import json
import pathlib
from importlib import resources
from typing import List, Mapping, Set

from organization_name_knowledge.knowledge_base.common_terms import CommonTerms
from organization_name_knowledge.knowledge_base.countries import Countries
from organization_name_knowledge.knowledge_base.legal_terms import LegalTerms


class classproperty:
    def __init__(self, f):
        self.f = f

    def __get__(self, obj, owner):
        return self.f(owner)


class KnowledgeBase:
    with resources.path("organization_name_knowledge", "resources") as path:
        default_source_path: pathlib.Path = path

    source_paths: List[pathlib.Path] = [default_source_path]
    _loaded = {}

    @classmethod
    def set_additional_source_paths(cls, dirs=()):
        cls.source_paths = [*dirs, cls.default_source_path]
        cls._loaded = {}

    @classmethod
    def get_file(cls, name):
        for dir in cls.source_paths:
            if (dir / name).exists():
                return dir / name
        raise FileNotFoundError(name)

    @classmethod
    def _get(cls, file_name):
        with cls.get_file(file_name).open("rt", encoding="utf-8") as f:
            return json.load(f)

    @classmethod
    def get(cls, key, callable=lambda x: x):
        if key not in cls._loaded:
            cls._loaded[key] = callable(cls._get(f"{key}.json"))
        return cls._loaded[key]

    @classproperty
    def common_suffixes(cls) -> CommonTerms:
        return cls.get(
            "common_suffixes",
            lambda x: CommonTerms(x, cls.weak_words),
        )

    @classproperty
    def joining_words(cls) -> Set:
        return cls.get("joining_words", set)

    @classproperty
    def weak_words(cls) -> Set:
        return cls.get("weak_words", set)

    @classproperty
    def common_prefixes(cls) -> CommonTerms:
        return cls.get(
            "common_prefixes",
            lambda x: CommonTerms(x, cls.weak_words),
        )

    @classproperty
    def legal_terms(cls) -> LegalTerms:
        return cls.get("legal_terms", LegalTerms)

    @classproperty
    def countries(cls) -> LegalTerms:
        return cls.get("countries", Countries)

    @classproperty
    def words_mapping(cls) -> Mapping[str, str]:
        return cls.get("words_mapping")
