import collections
from typing import Any, Sequence, Tuple


class TokensSequence(collections.UserList):
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
        return self.cleaned_name.endswith(seq.__str__().lower())

    def __eq__(self, other: Any) -> bool:
        if isinstance(other, Sequence):
            return self.data == list(other)
        else:
            raise NotImplementedError()

    def __str__(self) -> str:
        return " ".join(map(str, self.data))

    def __repr__(self) -> str:
        return f"TokensSequence({self.data!r})"

    def __bool__(self) -> bool:
        return any(self.data)
