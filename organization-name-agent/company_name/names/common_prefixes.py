from .special_words import WEAK_WORDS
from company_name.utils.clear_name import divide

_COMMON_PREFIXES = {
    "company",
    "group",
    "grup",
    "grupo",
    *WEAK_WORDS,
}

COMMON_PREFIXES = {tuple(divide(n)) for n in _COMMON_PREFIXES}
