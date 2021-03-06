import dataclasses
from typing import List, Tuple

from bank_identification_codes.codes_check.text_utils import (
    get_first_match,
    is_headquarters,
    remove_no_word_characters,
    remove_words_separators,
)
from bank_identification_codes.solution.reasons import (
    MatchingTextDoesNotMatchMatchingFieldReason,
    MatchingTextDoesNotMatchWlSearchCodeReason,
    MatchingTextIsOnlyPartialMatchForSearchCodeReason,
    MatchingTextIsPartOfLongerSequenceReason,
    MatchingTextMatchesWlBicCodeReason,
    MatchingTextMatchesWlSearchCodeReason,
    MatchingTextTooShortToBeCodeReason,
    NoSearchCodeInWatchlistReason,
)
from bank_identification_codes.solution.result import Reason, Result, Solution


@dataclasses.dataclass
class BankIdentificationCodes:
    alerted_party_matching_field: str
    watchlist_matching_text: str
    watchlist_type: str
    watchlist_search_codes: List[str]
    watchlist_bic_codes: List[str]

    def check(self) -> Result:
        if len(self.watchlist_search_codes) == 0 and len(self.watchlist_bic_codes) == 0:
            return Result(solution=Solution.NO_DECISION, reason=NoSearchCodeInWatchlistReason())

        if len(self.watchlist_matching_text) < 3:
            return Result(
                solution=Solution.NO_DECISION,
                reason=MatchingTextTooShortToBeCodeReason(self.watchlist_matching_text),
            )

        wl_matching_text_cleaned = remove_no_word_characters(self.watchlist_matching_text).upper()

        ap_matched_text = get_first_match(
            wl_matching_text_cleaned, self.alerted_party_matching_field
        )

        solution = Solution.NO_DECISION
        reason = MatchingTextDoesNotMatchMatchingFieldReason(
            self.watchlist_matching_text, self.alerted_party_matching_field
        )

        if ap_matched_text:
            solution, reason = self._search_in_codes(ap_matched_text, wl_matching_text_cleaned)

        return Result(solution=solution, reason=reason)

    def _search_in_codes(
        self, ap_matched_text: str, wl_matching_text_cleaned: str
    ) -> Tuple[Solution, Reason]:

        ap_matched_text_no_separators = remove_words_separators(ap_matched_text)
        solution, reason = self._search_in_bic_codes(
            ap_matched_text_no_separators, wl_matching_text_cleaned
        )
        if solution == Solution.NO_DECISION:
            solution, reason = self._search_in_search_codes(
                ap_matched_text,
                ap_matched_text_no_separators,
                wl_matching_text_cleaned,
            )
        return solution, reason

    def _search_in_bic_codes(
        self, ap_matched_text_no_separators: str, wl_matching_text_cleaned: str
    ) -> Tuple[Solution, Reason]:

        solution = Solution.NO_DECISION
        reason = None
        for bic_code in self.watchlist_bic_codes:
            bic_code = bic_code.strip().upper()
            matching_text_in_bic_code = wl_matching_text_cleaned in bic_code
            if is_headquarters(ap_matched_text_no_separators) and matching_text_in_bic_code:
                solution = Solution.NO_MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.watchlist_matching_text, bic_code, self.watchlist_type
                )
                break
            elif matching_text_in_bic_code:
                solution = Solution.MATCH
                reason = MatchingTextMatchesWlBicCodeReason(
                    self.watchlist_matching_text, bic_code, self.watchlist_type
                )
                break
        return solution, reason

    def _search_in_search_codes(
        self,
        ap_matched_text: str,
        ap_matched_text_no_separators: str,
        wl_matching_text_cleaned: str,
    ) -> Tuple[Solution, Reason]:

        search_codes = [search_code.strip().upper() for search_code in self.watchlist_search_codes]
        solution = Solution.NO_DECISION
        reason = MatchingTextDoesNotMatchWlSearchCodeReason(
            self.watchlist_matching_text, search_codes, self.watchlist_type
        )

        for search_code in search_codes:
            matching_text_in_search_code = wl_matching_text_cleaned in search_code

            if ap_matched_text_no_separators == search_code:
                solution = Solution.NO_MATCH
                reason = MatchingTextMatchesWlSearchCodeReason(
                    self.watchlist_matching_text, [search_code], self.watchlist_type
                )
                break

            elif (
                len(wl_matching_text_cleaned) < len(ap_matched_text_no_separators)
                and matching_text_in_search_code
            ):
                solution = Solution.MATCH
                reason = MatchingTextIsPartOfLongerSequenceReason(
                    self.watchlist_matching_text,
                    ap_matched_text,
                    self.alerted_party_matching_field,
                    search_code,  # here it goes as a 'partial match text'
                    self.watchlist_type,
                )
                break

            elif matching_text_in_search_code:
                solution = Solution.MATCH
                reason = MatchingTextIsOnlyPartialMatchForSearchCodeReason(
                    self.watchlist_matching_text, [search_code], self.watchlist_type
                )
                break
        return solution, reason
