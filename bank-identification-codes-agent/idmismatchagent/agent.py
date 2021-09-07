import re
from typing import Tuple

from attr import attrs, attrib

from idmismatchagent.api import (
    SearchCodeMismatchAgentInput,
    Result,
    Reason,
    NoSearchCodeInWatchlistReason,
    MatchingTextTooShortToBeCodeReason,
    MatchingTextMatchesWlSearchCodeReason,
    MatchingTextDoesNotMatchMatchingFieldReason,
    MatchingTextIsPartOfLongerSequenceReason,
    MatchingTextIsOnlyPartialMatchForSearchCodeReason,
    MatchingTextDoesNotMatchWlSearchCodeReason,
    MatchingTextMatchesWlBicCodeReason,
)

__all__ = ["identification_mismatch_agent"]

SEPARATORS_PATTERN = re.compile(r"[-),./]+")
HEADQUARTERS_INDICATOR = "XXX"


def identification_mismatch_agent(
    agent_input: SearchCodeMismatchAgentInput,
) -> Tuple[Result, Reason]:
    logic = IdMismatchLogic(
        agent_input.matching_field,
        agent_input.matching_text,
        agent_input.wl_type,
        _filter_none_values(agent_input.wl_search_codes),
        _filter_none_values(agent_input.wl_bic_codes),
    )

    return logic.execute()


@attrs(frozen=True)
class IdMismatchLogic:
    matching_field: str = attrib()
    matching_text: str = attrib()
    wl_type: str = attrib()
    wl_search_codes: list = attrib()
    wl_bic_codes: list = attrib()

    def execute(self) -> Tuple[Result, Reason]:
        if len(self.wl_search_codes) == 0 and len(self.wl_bic_codes) == 0:
            return Result.NO_DECISION, NoSearchCodeInWatchlistReason()

        if len(self.matching_text) < 3:
            return Result.NO_DECISION, MatchingTextTooShortToBeCodeReason(self.matching_text)

        matching_text_no_extra_characters = re.sub(r"\W+", "", self.matching_text).upper()

        matching_text_pattern = "[ ]{0,1}".join(matching_text_no_extra_characters)

        matching_field_no_extra_characters = _remove_ids_separators(self.matching_field)

        search_codes = [search_code.strip().upper() for search_code in self.wl_search_codes]
        bic_codes = [bic_code.strip().upper() for bic_code in self.wl_bic_codes]

        pattern = fr"""
        ([\w]{{0,}}                 # Matches 0 to inf characters before actual pattern (no spaces)
        ({matching_text_pattern})   # 2nd capture group for matching actual pattern
        [\w]{{0,}})                 # Matches 0 to inf characters after actual pattern
        """
        matching_text_in_field_match = re.search(
            pattern, matching_field_no_extra_characters, re.VERBOSE
        )

        result = Result.NO_DECISION
        reason = MatchingTextDoesNotMatchMatchingFieldReason(
            self.matching_text, self.matching_field
        )

        if matching_text_in_field_match:
            raw_matched_string = matching_text_in_field_match.group(0)
            whole_string = raw_matched_string.replace(" ", "").replace(",", "").replace("-", "")

            reason, result = self._search_in_bic_codes(
                bic_codes, matching_text_no_extra_characters, whole_string
            )
            if result == Result.NO_DECISION:
                reason, result = self._search_in_search_codes(
                    matching_text_no_extra_characters,
                    raw_matched_string,
                    search_codes,
                    whole_string,
                )

        return result, reason

    def _search_in_search_codes(
        self,
        matching_text_no_extra_characters,
        raw_matched_string,
        search_codes,
        whole_string,
    ):
        result = Result.NO_DECISION
        reason = MatchingTextDoesNotMatchWlSearchCodeReason(
            self.matching_text, search_codes, self.wl_type
        )
        for search_code in search_codes:
            if whole_string == search_code:
                result = Result.NO_MATCH
                reason = MatchingTextMatchesWlSearchCodeReason(
                    self.matching_text, search_code, self.wl_type
                )
                break

            elif (
                len(matching_text_no_extra_characters) < len(whole_string)
                and search_code.find(matching_text_no_extra_characters) != -1
            ):
                result = Result.MATCH
                reason = MatchingTextIsPartOfLongerSequenceReason(
                    self.matching_text,
                    raw_matched_string,
                    self.matching_field,
                    search_code,
                    self.wl_type,
                )
                break

            elif search_code.find(matching_text_no_extra_characters) != -1:
                result = Result.MATCH
                reason = MatchingTextIsOnlyPartialMatchForSearchCodeReason(
                    self.matching_text, search_code, self.wl_type
                )
                break
        return reason, result

    def _search_in_bic_codes(self, bic_codes, matching_text_no_extra_characters, whole_string):
        result = Result.NO_DECISION
        reason = None
        for bic_code in bic_codes:
            if (
                _is_headquarters(whole_string)
                and bic_code.find(matching_text_no_extra_characters) != -1
            ):
                result = Result.NO_MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.matching_text, bic_code, self.wl_type
                )
                break
            elif bic_code.find(matching_text_no_extra_characters) != -1:
                result = Result.MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.matching_text, bic_code, self.wl_type
                )
                break
        return reason, result


def _remove_ids_separators(text: str):
    return SEPARATORS_PATTERN.sub("", text).strip().upper()


def _is_headquarters(bic_code):
    return bic_code is not None and len(bic_code) > 0 and bic_code.endswith(HEADQUARTERS_INDICATOR)


def _filter_none_values(data: list):
    return list(filter(lambda el: el.upper() != "NONE", data))
