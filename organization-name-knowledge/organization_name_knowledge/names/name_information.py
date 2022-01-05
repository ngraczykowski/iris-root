import dataclasses
from typing import Set

from organization_name_knowledge.knowledge_base import KnowledgeBase
from organization_name_knowledge.names.token import Token
from organization_name_knowledge.names.tokens_sequence import TokensSequence


@dataclasses.dataclass
class NameInformation:
    source: Token
    common_prefixes: TokensSequence
    base: TokensSequence
    common_suffixes: TokensSequence
    legal: TokensSequence
    countries: TokensSequence
    parenthesis: TokensSequence
    other: TokensSequence

    def name(self) -> TokensSequence:
        return TokensSequence(self.common_prefixes + self.base + self.common_suffixes)

    def combined_name(self) -> TokensSequence:
        other_without_weak = [
            token for token in self.other if token not in KnowledgeBase.weak_words
        ]
        return self.name() + other_without_weak + self.parenthesis

    def tokens(self) -> Set[Token]:
        return set(
            self.common_prefixes
            + self.base
            + self.common_suffixes
            + self.legal
            + self.other
            + self.countries
            + self.parenthesis
        )

    def __str__(self) -> str:
        return (
            self.source.original
            + " ("
            + ", ".join(
                [
                    f"{name}: {getattr(self, name)}"
                    for name in dataclasses.asdict(self)
                    if getattr(self, name) and name != "source"
                ]
            )
            + ")"
        )

    def __bool__(self) -> bool:
        return bool(self.source)

    def __repr__(self) -> str:
        return (
            "NameInformation("
            + ", ".join(f"{name}={getattr(self, name)!r}" for name in dataclasses.asdict(self))
            + ")"
        )

    def __hash__(self):
        return hash(self.source)

    def dict(self):
        d = {field: self.__getattribute__(field).dict() for field in vars(self)}
        return d
