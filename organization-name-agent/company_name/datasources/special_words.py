import json

import importlib_resources

JOINING_WORDS_PATH = (
    importlib_resources.files("company_name") / "resources" / "joining_words.json"
)

with JOINING_WORDS_PATH.open("rt", encoding="utf-8") as f:
    JOINING_WORDS = set(json.load(f))


WEAK_WORDS_PATH = (
    importlib_resources.files("company_name") / "resources" / "weak_words.json"
)

with WEAK_WORDS_PATH.open("rt", encoding="utf-8") as f:
    WEAK_WORDS = set(json.load(f))
