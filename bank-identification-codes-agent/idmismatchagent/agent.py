import re
from typing import Tuple

from attr import attrs, attrib

from .api import *

__all__ = ["identification_mismatch_agent"]

from .comments import CommentGenerator


def identification_mismatch_agent(
    agent_input: SearchCodeMismatchAgentInput,
    config: SearchCodeMismatchAgentConfig = SearchCodeMismatchAgentConfig(),
) -> Tuple[Result, Reason, str]:
    logic = IdMismatchLogic(
        agent_input.matching_field,
        agent_input.matching_text,
        agent_input.wl_type,
        agent_input.wl_search_codes,
    )
    result, reason = logic.execute()

    comment_generator = CommentGenerator(
        config.templates_dir, config.comment_timeout, config.regex_timeout, reason
    )
    comment = comment_generator.generate()

    if comment is None:
        return result, reason, "COMMENT GENERATION ERROR"

    return result, reason, comment


@attrs(frozen=True)
class IdMismatchLogic:
    matching_field: str = attrib()
    matching_text: str = attrib()
    wl_type: str = attrib()
    wl_search_codes: str = attrib()

    def execute(self) -> Tuple[Result, Reason]:
        if (
            self.wl_search_codes is None
            or len(self.wl_search_codes) == 0
            or self.wl_search_codes == "none"
            or self.wl_search_codes == "None"
        ):
            return Result.NO_DECISION, NoSearchCodeInWatchlistReason()

        if len(self.matching_text) < 3:
            return Result.NO_DECISION, MatchingTextTooShortToBeCodeReason(self.matching_text)

        remove_non_word_chars = lambda text: re.sub(r"\W+", "", text)
        matching_text_no_extra_characters = remove_non_word_chars(self.matching_text).upper()

        matching_text_pattern = "[ ]{0,1}".join(matching_text_no_extra_characters)

        matching_field_no_extra_characters = _remove_ids_separators(self.matching_field)

        search_codes = [search_code.upper() for search_code in self.wl_search_codes.split()]

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
            reason = MatchingTextDoesNotMatchWlSearchCodeReason(
                self.matching_text, search_codes, self.wl_type
            )

            raw_matched_string = matching_text_in_field_match.group(0)
            whole_string = raw_matched_string.replace(" ", "").replace(",", "").replace("-", "")

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

        return result, reason


def _remove_ids_separators(text: str):
    return re.sub(r"[-\),\.\/]+", "", text).strip().upper()
