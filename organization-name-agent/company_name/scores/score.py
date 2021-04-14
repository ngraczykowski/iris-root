import dataclasses
from typing import Tuple, Any


@dataclasses.dataclass
class Score:
    value: float
    compared: Tuple[Tuple[str, ...], Tuple[str, ...]]

    def __lt__(self, other: Any) -> bool:
        if hasattr(other, "value"):
            return self.value.__lt__(other.value)
        else:
            return self.value.__lt__(other)

    def __gt__(self, other: Any) -> bool:
        if hasattr(other, "value"):
            return self.value.__gt__(other.value)
        else:
            return self.value.__gt__(other)

    def __imul__(self, other: Any) -> "Score":
        if isinstance(other, float):
            self.value *= other
            return self
        raise NotImplementedError()

    def __mul__(self, other: Any) -> "Score":
        if isinstance(other, float):
            return Score(value=self.value * other, compared=self.compared)

        raise NotImplementedError()

    def __reversed__(self) -> "Score":
        return Score(value=self.value, compared=(self.compared[1], self.compared[0]))
