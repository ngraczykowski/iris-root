import re
from typing import Tuple

from attr import attrib, attrs

from data_models.reasons import (
    MatchingTextDoesNotMatchMatchingFieldReason,
    MatchingTextDoesNotMatchWlSearchCodeReason,
    MatchingTextIsOnlyPartialMatchForSearchCodeReason,
    MatchingTextIsPartOfLongerSequenceReason,
    MatchingTextMatchesWlBicCodeReason,
    MatchingTextMatchesWlSearchCodeReason,
    MatchingTextTooShortToBeCodeReason,
    NoSearchCodeInWatchlistReason,
)
from data_models.result import Reason, Result, Solution
from idmismatchagent.utils import _is_headquarters, _remove_ids_separators, get_text_pattern


@attrs(frozen=True)
class BankIdentificationCodes:
    altered_party_matching_field: str = attrib()
    watchlist_matching_text: str = attrib()
    watchlist_type: str = attrib()
    watchlist_search_codes: list = attrib()
    watchlist_bic_codes: list = attrib()

    def check(self) -> Result:
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

        pattern = get_text_pattern(text=matching_text_no_extra_characters)

        matching_field_no_extra_characters = _remove_ids_separators(self.altered_party_matching_field)

        search_codes = [search_code.strip().upper() for search_code in self.watchlist_search_codes]
        bic_codes = [bic_code.strip().upper() for bic_code in self.watchlist_bic_codes]

        matching_text_in_field_match = re.search(
            pattern, matching_field_no_extra_characters, re.VERBOSE
        )

        solution = Solution.NO_DECISION
        reason = MatchingTextDoesNotMatchMatchingFieldReason(
            self.watchlist_matching_text, self.altered_party_matching_field
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
                    self.altered_party_matching_field,
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
            matching_text_in_bic_code_place = bic_code.find(matching_text_no_extra_characters)
            if _is_headquarters(whole_string) and matching_text_in_bic_code_place != -1:
                solution = Solution.NO_MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.watchlist_matching_text, bic_code, self.watchlist_type
                )
                break
            elif matching_text_in_bic_code_place -1:
                solution = Solution.MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.watchlist_matching_text, bic_code, self.watchlist_type
                )
                break
        return solution, reason
