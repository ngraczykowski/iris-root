import json
import pathlib

from company_name.utils.clear_name import divide
from company_name.datasources.term_sources import TermSources
from company_name.datasources.special_words import WEAK_WORDS

COMMON_SUFFIXES_PATH = pathlib.Path(__file__).parent / "common_suffixes.json"

with COMMON_SUFFIXES_PATH.open("rt", encoding="utf-8") as f:
    COMMON_SUFFIXES = TermSources({tuple(divide(n)) for n in (*json.load(f), *WEAK_WORDS)})
