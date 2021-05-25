import collections
import dataclasses
from typing import Tuple, Sequence, Any


@dataclasses.dataclass
class NameWord:
    original: str
    cleaned: str

    def __eq__(self, other: Any) -> bool:
        if isinstance(other, str):
            return self.cleaned == other
        if hasattr(other, "cleaned"):
            return bool(self.cleaned == other.cleaned)
        raise NotImplementedError()

    def __add__(self, other: "NameWord") -> "NameWord":
        if not isinstance(other, NameWord):
            raise NotImplementedError()

        return NameWord(
            original=f"{self.original} {other.original}",
            cleaned=f"{self.cleaned} {other.cleaned}",
        )

    @staticmethod
    def join(*seq: "NameWord") -> "NameWord":
        return NameWord(
            original=" ".join(n.original for n in seq),
            cleaned=" ".join(n.cleaned for n in seq),
        )

    def __str__(self) -> str:
        return self.original

    def __repr__(self) -> str:
        return (
            "NameWord("
            + ", ".join(
                f"{name}={getattr(self, name)!r}" for name in dataclasses.asdict(self)
            )
            + ")"
        )

    def __hash__(self) -> int:
        return self.cleaned.__hash__()

    def __bool__(self) -> bool:
        return bool(self.cleaned)


class NameSequence(collections.UserList):
    @property
    def original_name(self) -> str:
        return " ".join((w.original for w in self.data if w.original))

    @property
    def cleaned_name(self) -> str:
        return " ".join((w.cleaned for w in self.data if w.cleaned))

    @property
    def cleaned_tuple(self) -> Tuple[str, ...]:
        return tuple(w.cleaned for w in self.data)

    @property
    def original_tuple(self) -> Tuple[str, ...]:
        return tuple(w.original for w in self.data)

    def endswith(self, seq: Sequence[str]) -> bool:
        if not isinstance(seq, Sequence):
            raise NotImplementedError()

        if len(self) < len(seq):
            return False

        return self.data[-len(seq) :] == list(seq)

    def startswith(self, seq: Sequence[str]) -> bool:
        if not isinstance(seq, Sequence):
            raise NotImplementedError()

        if len(self) < len(seq):
            return False

        return self.data[: len(seq)] == list(seq)

    def __eq__(self, other: Any) -> bool:
        if isinstance(other, Sequence):
            return self.data == list(other)
        else:
            raise NotImplementedError()

    def __str__(self) -> str:
        return " ".join(map(str, self.data))

    def __repr__(self) -> str:
        return f"NameSequence({self.data!r})"

    def __bool__(self) -> bool:
        return any(self.data)


@dataclasses.dataclass
class NameInformation:
    source: NameWord

    common_prefixes: NameSequence
    base: NameSequence
    common_suffixes: NameSequence
    legal: NameSequence
    countries: NameSequence
    parenthesis: NameSequence
    other: NameSequence

    def name(self) -> NameSequence:
        return NameSequence(self.common_prefixes + self.base + self.common_suffixes)

    def __str__(self) -> str:
        return (
            self.source.original
            + " ("
            + ", ".join(
                [
                    f"{name}: {value}"
                    for name, value in dataclasses.asdict(self).items()
                    if value and name != "source"
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
