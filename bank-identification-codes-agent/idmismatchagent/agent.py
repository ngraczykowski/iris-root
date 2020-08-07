import re
import sys
import logging

from typing import Tuple
from utils import agent_timeout, catch_and_return
from .api import Result, Reason, NoSearchCodeInWatchlistReason, MatchingTextTooShortToBeCodeReason, \
    MatchingTextMatchesWlSearchCodeReason, MatchingTextIsPartOfLongerSequenceReason, \
    MatchingTextIsOnlyPartialMatchForSearchCodeReason, MatchingTextDoesNotMatchWlSearchCodeReason, \
    SearchCodeMismatchAgentInput, MatchingTextDoesNotMatchMatchingFieldReason
from .comments import get_comment
from utils import LogLevel, generate_logger


@catch_and_return(Exception, (Result.AGENT_ERROR.value, "Agent error"))
@agent_timeout
def search_code_mismatch_agent(input: SearchCodeMismatchAgentInput) -> Tuple[str, str]:
    wl_search_codes = input.wl_search_codes
    matching_text = input.matching_text
    matching_field_value = input.matching_field
    wl_type = input.wl_type

    if wl_search_codes is None or len(wl_search_codes) == 0 or wl_search_codes == "none" or wl_search_codes == "None":
        return _output(Result.NO_DECISION, NoSearchCodeInWatchlistReason())

    if len(matching_text) < 3:
        return _output(Result.NO_DECISION, MatchingTextTooShortToBeCodeReason(matching_text))

    remove_non_word_chars = lambda text: re.sub(r'\W+', '', text)
    matching_text_no_extra_characters = remove_non_word_chars(matching_text).upper()

    matching_text_pattern = "[ ]{0,1}".join(matching_text_no_extra_characters)

    matching_field_no_extra_characters = __remove_ids_separators(matching_field_value)

    search_codes = [search_code.upper() for search_code in wl_search_codes.split()]

    pattern = fr"""
    ([\w]{{0,}}                 # Matches 0 to inf characters before actual pattern (no spaces) 
    ({matching_text_pattern})   # 2nd capture group for matching actual pattern
    [\w]{{0,}})                 # Matches 0 to inf characters after actual pattern
    """
    matching_text_in_field_match = re.search(pattern, matching_field_no_extra_characters, re.VERBOSE)

    if matching_text_in_field_match:
        raw_matched_string = matching_text_in_field_match.group(0)
        whole_string = raw_matched_string.replace(" ", "").replace(",", "").replace("-", "")
        for search_code in search_codes:
            if whole_string == search_code:
                return _output(
                    Result.NO_MATCH,
                    MatchingTextMatchesWlSearchCodeReason(matching_text, search_code, wl_type)
                )
            elif len(matching_text_no_extra_characters) < len(whole_string) \
                    and search_code.find(matching_text_no_extra_characters) != -1:
                return _output(
                    Result.MATCH,
                    MatchingTextIsPartOfLongerSequenceReason(
                        matching_text, raw_matched_string, matching_field_value, search_code, wl_type
                    )
                )
            elif search_code.find(matching_text_no_extra_characters) != -1:
                return _output(
                    Result.MATCH,
                    MatchingTextIsOnlyPartialMatchForSearchCodeReason(matching_text, search_code, wl_type))
        return _output(
            Result.NO_DECISION,
            MatchingTextDoesNotMatchWlSearchCodeReason(matching_text, search_codes, wl_type)
        )

    return _output(
        Result.NO_DECISION,
        MatchingTextDoesNotMatchMatchingFieldReason(matching_text, matching_field_value)
    )


def __remove_ids_separators(text: str):
    return re.sub(r'[-\),\.\/]+', '', text).strip().upper()


def _output(result: Result, reason: Reason) -> Tuple[str, str]:
    comment = get_comment(reason)

    if comment is None:
        return result.value, "COMMENT GENERATION ERROR"

    return result.value, comment


if __name__ == '__main__':
    logger = generate_logger(log_level=LogLevel.debug)
    logger.debug(search_code_mismatch_agent(SearchCodeMismatchAgentInput(
        message_type="103",
        message_tag="70",
        matching_field="WE REFER TO 23190617054158 FOR15,990.00",
        matching_text="190617",
        wl_search_codes="190617",
        wl_type="Individual"
    )))
