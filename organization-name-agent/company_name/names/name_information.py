import dataclasses

from company_name.names.token import Token
from company_name.names.tokens_sequence import TokensSequence


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
            + ", ".join(
                f"{name}={getattr(self, name)!r}" for name in dataclasses.asdict(self)
            )
            + ")"
        )
