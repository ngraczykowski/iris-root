import logging
import re
from typing import List

from organization_name_knowledge import parse


def get_company_token_count(ap_names: List[str]):
    if ap_names is None or len(ap_names) != 1:
        return 0
    else:
        ap_name = ap_names[0]
        if is_ascii(ap_name):
            name_info = parse(ap_name)
            legal = name_info.legal
            prefix = name_info.common_prefixes
            suffix = name_info.common_suffixes

            return len(legal) + len(prefix) + len(suffix)
        else:
            return 0


def is_ascii(text: str) -> bool:
    try:
        if re.search(r"[^\x00-\x7F]", text):
            return False
        else:
            return True
    except Exception as exc:
        logging.error(exc)
        pass
