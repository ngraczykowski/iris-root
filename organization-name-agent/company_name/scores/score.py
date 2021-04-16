import dataclasses
from typing import Tuple, Any, Optional


@dataclasses.dataclass
class Score:
    value: Optional[float] = None
    compared: Tuple[Tuple[str, ...], Tuple[str, ...]] = ((), ())

    def __lt__(self, other: Any) -> bool:
        if not self.value:
            return True

        if hasattr(other, "value"):
            return self.value.__lt__(other.value)
        else:
            return self.value.__lt__(other)

    def __gt__(self, other: Any) -> bool:
        if not self.value:
            return False

        if hasattr(other, "value"):
            return self.value.__gt__(other.value)
        else:
            return self.value.__gt__(other)

    def __imul__(self, other: Any) -> "Score":
        if isinstance(other, float) or isinstance(other, int):
            self.value *= other
            return self
        raise NotImplementedError()

    def __mul__(self, other: Any) -> "Score":
        if isinstance(other, float) or isinstance(other, int):
            return Score(value=self.value * other, compared=self.compared)

        raise NotImplementedError()

    def __reversed__(self) -> "Score":
        return Score(value=self.value, compared=(self.compared[1], self.compared[0]))
