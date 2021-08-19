import dataclasses
from typing import Any


@dataclasses.dataclass
class Token:
    original: str
    cleaned: str

    def __eq__(self, other: Any) -> bool:
        if isinstance(other, str):
            return self.cleaned == other
        if hasattr(other, "cleaned"):
            return bool(self.cleaned == other.cleaned)
        raise NotImplementedError()

    def __add__(self, other: "Token") -> "Token":
        if not isinstance(other, Token):
            raise NotImplementedError()

        return Token(
            original=f"{self.original} {other.original}",
            cleaned=f"{self.cleaned} {other.cleaned}",
        )

    @staticmethod
    def join(*seq: "Token") -> "Token":
        return Token(
            original=" ".join(n.original for n in seq),
            cleaned=" ".join(n.cleaned for n in seq),
        )

    def __str__(self) -> str:
        return self.original

    def __repr__(self) -> str:
        return (
            "Token("
            + ", ".join(f"{name}={getattr(self, name)!r}" for name in dataclasses.asdict(self))
            + ")"
        )

    def __hash__(self) -> int:
        return self.cleaned.__hash__()

    def __bool__(self) -> bool:
        return bool(self.cleaned)
