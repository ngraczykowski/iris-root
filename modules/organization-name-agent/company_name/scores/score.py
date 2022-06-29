import dataclasses
import enum
from typing import Any, Tuple


@dataclasses.dataclass
class Score:
    class ScoreStatus(enum.Enum):
        UNKNOWN = None
        NO_DATA = "NO_DATA"
        NO_ALERTED_PARTY_DATA = "NO_ALERTED_PARTY_DATA"
        NO_MATCHED_PARTY_DATA = "NO_MATCHED_PARTY_DATA"
        NOT_APPLICABLE = "NOT_APPLICABLE"
        OK = "OK"

        @classmethod
        def recognize(cls, alerted_party_data, matched_party_data) -> "Score.ScoreStatus":
            return {
                (True, True): cls.OK,
                (True, False): cls.NO_MATCHED_PARTY_DATA,
                (False, True): cls.NO_ALERTED_PARTY_DATA,
                (False, False): cls.NO_DATA,
            }[(bool(alerted_party_data), bool(matched_party_data))]

        def __lt__(self, other):
            return list(self.__class__).index(self) < list(self.__class__).index(other)

        def __repr__(self):
            return repr(self.value)

    status: ScoreStatus = ScoreStatus.UNKNOWN
    value: float = 0
    compared: Tuple[Tuple[str, ...], Tuple[str, ...]] = ((), ())

    def __post_init__(self):
        if self.status == self.ScoreStatus.UNKNOWN:
            self.status = self.ScoreStatus.recognize(*self.compared)

    def __lt__(self, other: Any) -> bool:
        if hasattr(other, "value"):
            return (self.value, self.status) < (
                other.value,
                getattr(other, "status", self.ScoreStatus.UNKNOWN),
            )
        else:
            return self.value.__lt__(other)

    def __gt__(self, other: Any) -> bool:
        if hasattr(other, "value"):
            return (self.value, self.status) > (
                other.value,
                getattr(other, "status", self.ScoreStatus.UNKNOWN),
            )
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

    def __repr__(self):
        return (
            "Score("
            + ", ".join(f"{name}={getattr(self, name)!r}" for name in dataclasses.asdict(self))
            + ")"
        )
