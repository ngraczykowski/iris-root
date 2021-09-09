import logging
import re
from typing import Tuple

from agent_base.agent import Agent
from attr import attrib, attrs

from idmismatchagent.api import (
    MatchingTextDoesNotMatchMatchingFieldReason,
    MatchingTextDoesNotMatchWlSearchCodeReason,
    MatchingTextIsOnlyPartialMatchForSearchCodeReason,
    MatchingTextIsPartOfLongerSequenceReason,
    MatchingTextMatchesWlBicCodeReason,
    MatchingTextMatchesWlSearchCodeReason,
    MatchingTextTooShortToBeCodeReason,
    NoSearchCodeInWatchlistReason,
    SearchCodeMismatchAgentInput,
)
from idmismatchagent.result import Reason, Result, Solution

SEPARATORS_PATTERN = re.compile(r"[-),./]+")
HEADQUARTERS_INDICATOR = "XXX"


class IdentificationMismatchAgent(Agent):
    def resolve(self, agent_input: SearchCodeMismatchAgentInput) -> Result:
        try:
            logic = IdMismatchLogic(
                agent_input.altered_party_matching_field,
                agent_input.watchlist_matching_text,
                agent_input.watchlist_type,
                _filter_none_values(agent_input.watchlist_search_codes),
                _filter_none_values(agent_input.watchlist_bic_codes),
            )

            return logic.execute()
        except Exception:
            logging.exception(f"For agent input {agent_input}")
            return Result(solution=Solution.AGENT_ERROR)


@attrs(frozen=True)
class IdMismatchLogic:
    matching_field: str = attrib()
    watchlist_matching_text: str = attrib()
    watchlist_type: str = attrib()
    watchlist_search_codes: list = attrib()
    watchlist_bic_codes: list = attrib()

    def execute(self) -> Result:
        if len(self.watchlist_search_codes) == 0 and len(self.watchlist_bic_codes) == 0:
            return Result(solution=Solution.NO_DECISION, reason=NoSearchCodeInWatchlistReason())

        if len(self.watchlist_matching_text) < 3:
            return Result(
                solution=Solution.NO_DECISION,
                reason=MatchingTextTooShortToBeCodeReason(self.watchlist_matching_text),
            )

        matching_text_no_extra_characters = re.sub(
            r"\W+", "", self.watchlist_matching_text
        ).upper()

        matching_text_pattern = "[ ]{0,1}".join(matching_text_no_extra_characters)

        matching_field_no_extra_characters = _remove_ids_separators(self.matching_field)

        search_codes = [search_code.strip().upper() for search_code in self.watchlist_search_codes]
        bic_codes = [bic_code.strip().upper() for bic_code in self.watchlist_bic_codes]

        pattern = fr"""
        ([\w]{{0,}}                 # Matches 0 to inf characters before actual pattern (no spaces)
        ({matching_text_pattern})   # 2nd capture group for matching actual pattern
        [\w]{{0,}})                 # Matches 0 to inf characters after actual pattern
        """
        matching_text_in_field_match = re.search(
            pattern, matching_field_no_extra_characters, re.VERBOSE
        )

        solution = Solution.NO_DECISION
        reason = MatchingTextDoesNotMatchMatchingFieldReason(
            self.watchlist_matching_text, self.matching_field
        )

        if matching_text_in_field_match:
            raw_matched_string = matching_text_in_field_match.group(0)
            whole_string = raw_matched_string.replace(" ", "").replace(",", "").replace("-", "")

            solution, reason = self._search_in_bic_codes(
                bic_codes, matching_text_no_extra_characters, whole_string
            )
            if solution == Solution.NO_DECISION:
                solution, reason = self._search_in_search_codes(
                    matching_text_no_extra_characters,
                    raw_matched_string,
                    search_codes,
                    whole_string,
                )

        return Result(solution=solution, reason=reason)

    def _search_in_search_codes(
        self,
        matching_text_no_extra_characters,
        raw_matched_string,
        search_codes,
        whole_string,
    ) -> Tuple[Solution, Reason]:
        solution = Solution.NO_DECISION
        reason = MatchingTextDoesNotMatchWlSearchCodeReason(
            self.watchlist_matching_text, search_codes, self.watchlist_type
        )
        for search_code in search_codes:
            if whole_string == search_code:
                solution = Solution.NO_MATCH
                reason = MatchingTextMatchesWlSearchCodeReason(
                    self.watchlist_matching_text, [search_code], self.watchlist_type
                )
                break

            elif (
                len(matching_text_no_extra_characters) < len(whole_string)
                and search_code.find(matching_text_no_extra_characters) != -1
            ):
                solution = Solution.MATCH
                reason = MatchingTextIsPartOfLongerSequenceReason(
                    self.watchlist_matching_text,
                    raw_matched_string,
                    self.matching_field,
                    search_code,
                    self.watchlist_type,
                )
                break

            elif search_code.find(matching_text_no_extra_characters) != -1:
                solution = Solution.MATCH
                reason = MatchingTextIsOnlyPartialMatchForSearchCodeReason(
                    self.watchlist_matching_text, [search_code], self.watchlist_type
                )
                break
        return solution, reason

    def _search_in_bic_codes(
        self, bic_codes, matching_text_no_extra_characters, whole_string
    ) -> Tuple[Solution, Reason]:
        solution = Solution.NO_DECISION
        reason = None
        for bic_code in bic_codes:
            if (
                _is_headquarters(whole_string)
                and bic_code.find(matching_text_no_extra_characters) != -1
            ):
                solution = Solution.NO_MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.watchlist_matching_text, bic_code, self.watchlist_type
                )
                break
            elif bic_code.find(matching_text_no_extra_characters) != -1:
                solution = Solution.MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.watchlist_matching_text, bic_code, self.watchlist_type
                )
                break
        return solution, reason


def _remove_ids_separators(text: str):
    return SEPARATORS_PATTERN.sub("", text).strip().upper()


def _is_headquarters(bic_code):
    return bic_code is not None and len(bic_code) > 0 and bic_code.endswith(HEADQUARTERS_INDICATOR)


def _filter_none_values(data: list):
    return list(filter(lambda el: el.upper() != "NONE", data))
