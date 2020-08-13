import re
import traceback
from typing import Dict, Callable

from attr import asdict, attrs, attrib

from tstoolkit.comments import get_and_render_template
from tstoolkit.utils import timeout, catch_and_return
from tstoolkit.utils.logger import generate_logger
from tstoolkit.utils.textextraction import try_extracting_iec, try_extracting_inv_no, get_context
from .api import (
    Reason,
    NoSearchCodeInWatchlistReason,
    MatchingTextMatchesWlSearchCodeReason,
    MatchingTextIsPartOfLongerSequenceReason,
    MatchingTextIsOnlyPartialMatchForSearchCodeReason,
    MatchingTextDoesNotMatchWlSearchCodeReason,
    MatchingTextDoesNotMatchMatchingFieldReason,
)
from .specific_text_extractors import try_extracting_acc_no, try_extracting_pan_no

logger = generate_logger()

template_by_reason: Dict[Reason, str] = {
    NoSearchCodeInWatchlistReason: "no-search-code-in-watchlist",
    MatchingTextMatchesWlSearchCodeReason: "matching-text-matches-wl-search-code",
    MatchingTextIsPartOfLongerSequenceReason: "matching-text-is-part-of-longer-sequence",
    MatchingTextIsOnlyPartialMatchForSearchCodeReason: "matching-text-is-only-partial-match-for-search-code",
    MatchingTextDoesNotMatchWlSearchCodeReason: "matching-text-does-not-match-wl-search-code",
    MatchingTextDoesNotMatchMatchingFieldReason: "matching-text-does-not-match-matching-field",
}

MATCHED_SEQUENCE_CONTEXT_SIZE = 20


@attrs(frozen=True)
class CommentGenerator:
    templates_dir: str = attrib()
    comment_timeout: int = attrib()
    call_timeout: int = attrib()
    reason: Reason = attrib()

    def generate(self):
        if isinstance(self.reason, MatchingTextIsPartOfLongerSequenceReason):
            values = self._get_longer_sequence_values()
        else:
            values = self._get_values()

        template_name = template_by_reason[type(self.reason)]

        try:
            return timeout(self.comment_timeout)(get_and_render_template)(
                template_name,
                values,
                templates_package_name="idmismatchagent",
                templates_searchpath=self.templates_dir,
            )
        except Exception as e:
            logger.debug(traceback.format_exc())
            return type(self.reason).__name__

    def _get_longer_sequence_values(self):
        matching_field_without_separators = (
            re.sub(r"[-\),\.\/]+", "", self.reason.matching_field).strip().upper()
        )
        matched_sequence = self.reason.raw_matched_sequence

        extracted_iec = self._safe_callable(try_extracting_iec)(matching_field_without_separators)
        if extracted_iec and (
            extracted_iec in matched_sequence or matched_sequence in extracted_iec
        ):
            return self._get_values(iec_match=extracted_iec)

        extracted_invoice_no = self._safe_callable(try_extracting_inv_no)(
            matching_field_without_separators
        )
        if extracted_invoice_no and (
            extracted_invoice_no in matched_sequence or matched_sequence in extracted_invoice_no
        ):
            return self._get_values(inv_no_match=extracted_invoice_no)

        extracted_acc_no = self._safe_callable(try_extracting_acc_no)(
            matching_field_without_separators
        )
        if extracted_acc_no and (
            extracted_acc_no in matched_sequence or matched_sequence in extracted_acc_no
        ):
            return self._get_values(acc_no_match=extracted_acc_no)

        extracted_pan_no = self._safe_callable(try_extracting_pan_no)(
            matching_field_without_separators
        )
        if extracted_pan_no and (
            extracted_pan_no in matched_sequence or matched_sequence in extracted_pan_no
        ):
            return self._get_values(pan_no_match=extracted_acc_no)

        matched_sequence_context: str = get_context(
            self.reason.raw_matched_sequence,
            matching_field_without_separators,
            MATCHED_SEQUENCE_CONTEXT_SIZE,
        )
        return self._get_values(matched_sequence_context=matched_sequence_context)

    def _get_values(self, **kwargs):
        values = {}
        values.update(asdict(self.reason))
        values.update(**kwargs)
        return values

    def _safe_callable(self, func: Callable[[any], any]) -> Callable[[any], any]:
        return catch_and_return(TimeoutError, None)(timeout(self.call_timeout)(func))
