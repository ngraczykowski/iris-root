import re
import traceback
from typing import Dict, Callable

from attr import asdict

from comments import get_and_render_template
from utils.textextraction import try_extracting_iec, try_extracting_inv_no, get_context
from utils import timeout, catch_and_return
from .api import Reason, NoSearchCodeInWatchlistReason, MatchingTextMatchesWlSearchCodeReason, \
    MatchingTextIsPartOfLongerSequenceReason, MatchingTextIsOnlyPartialMatchForSearchCodeReason, \
    MatchingTextDoesNotMatchWlSearchCodeReason, MatchingTextDoesNotMatchMatchingFieldReason
from .specific_text_extractors import try_extracting_acc_no, try_extracting_pan_no
from config import tsaas_main_config as cfg
from utils import generate_logger

templates_dir = "search-code-mismatch-agent/"

logger = generate_logger()

def template(name: str) -> str:
    return templates_dir + name


template_by_reason: Dict[Reason, str] = {
    NoSearchCodeInWatchlistReason: template("no-search-code-in-watchlist"),
    MatchingTextMatchesWlSearchCodeReason: template("matching-text-matches-wl-search-code"),
    MatchingTextIsPartOfLongerSequenceReason: template("matching-text-is-part-of-longer-sequence"),
    MatchingTextIsOnlyPartialMatchForSearchCodeReason: template("matching-text-is-only-partial-match-for-search-code"),
    MatchingTextDoesNotMatchWlSearchCodeReason: template("matching-text-does-not-match-wl-search-code"),
    MatchingTextDoesNotMatchMatchingFieldReason: template("matching-text-does-not-match-matching-field")
}

MATCHED_SEQUENCE_CONTEXT_SIZE = 20


def safe_regex_extraction(func: Callable[[any], any]) -> Callable[[any], any]:
    return catch_and_return(TimeoutError, None)(timeout(cfg.COMMENTS.REGEX_TIMEOUT)(func))


def longer_sequence_comment(reason: MatchingTextIsPartOfLongerSequenceReason) -> str:
    template_path = template_by_reason[MatchingTextIsPartOfLongerSequenceReason]

    matching_field_without_separators = re.sub(r'[-\),\.\/]+', '', reason.matching_field).strip().upper()
    matched_sequence = reason.raw_matched_sequence

    extracted_iec = safe_regex_extraction(try_extracting_iec)(matching_field_without_separators)
    if extracted_iec and (extracted_iec in matched_sequence or matched_sequence in extracted_iec):
        return get_and_render_template(template_path, {**asdict(reason), "iec_match": extracted_iec})

    extracted_invoice_no = safe_regex_extraction(try_extracting_inv_no)(matching_field_without_separators)
    if extracted_invoice_no and (extracted_invoice_no in matched_sequence or matched_sequence in extracted_invoice_no):
        return get_and_render_template(template_path, {**asdict(reason), "inv_no_match": extracted_invoice_no})

    extracted_acc_no = safe_regex_extraction(try_extracting_acc_no)(matching_field_without_separators)
    if extracted_acc_no and (extracted_acc_no in matched_sequence or matched_sequence in extracted_acc_no):
        return get_and_render_template(template_path, {**asdict(reason), "acc_no_match": extracted_acc_no})

    extracted_pan_no = safe_regex_extraction(try_extracting_pan_no)(matching_field_without_separators)
    if extracted_pan_no and (extracted_pan_no in matched_sequence or matched_sequence in extracted_pan_no):
        return get_and_render_template(template_path, {**asdict(reason), "pan_no_match": extracted_acc_no})

    matched_sequence_context: str = get_context(
        reason.raw_matched_sequence, matching_field_without_separators, MATCHED_SEQUENCE_CONTEXT_SIZE)
    return get_and_render_template(template_path,
                                   {**asdict(reason), "matched_sequence_context": matched_sequence_context})


@timeout(cfg.COMMENTS.COMMENT_GENERATION_TIMEOUT)
def get_comment(reason: Reason) -> str:
    reason_is = lambda reason_type: isinstance(reason, reason_type)

    if reason_is(MatchingTextIsPartOfLongerSequenceReason):
        return longer_sequence_comment(reason)

    try:
        return get_and_render_template(template_by_reason[type(reason)], asdict(reason))
    except Exception as e:
        logger.debug(traceback.format_exc())
        return type(reason).__name__
