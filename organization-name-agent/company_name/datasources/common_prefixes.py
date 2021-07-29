import json

import importlib_resources

from company_name.datasources.special_words import WEAK_WORDS
from company_name.datasources.term_sources import TermSources
from company_name.utils.clear_name import divide

COMMON_PREFIXES_PATH = (
    importlib_resources.files("company_name") / "resources" / "common_prefixes.json"
)

with COMMON_PREFIXES_PATH.open("rt", encoding="utf-8") as f:
    COMMON_PREFIXES = TermSources(
        {tuple(divide(n)) for n in (*json.load(f), *WEAK_WORDS)}
    )
