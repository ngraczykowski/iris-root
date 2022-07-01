from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Any, Dict, List, Optional, Union


@dataclass
class ValueKnowledge:
    """
    Parameters
    ----------
    original_input : str
        The exact piece of input data, which created the following results
    results : Any
         An object containing the knowledge results acquired from the original_input
    domain_name: Optional[str] = None
    """

    original_input: str
    results: Any
    domain_name: Optional[str] = None


@dataclass
class ValueMeasure:
    """
    Parameters
    ----------
    ap_value: str
        The value of a field entry corresponding to one side of the measure
        (canonically the alerted party side)
    wl_value: str
        The value of a field entry corresponding to other side of the measure
        (canonically the watchlist party side)
    evaluation: str
        Contains the evaluation of the measure between ap_value and wl_value
    metrics: Any = None
        Metrics used to produce the evaluation
    ignore: bool = False
        A flag indicating whether this measure should be ignored in the analysis
    ignore_reason: str = ""
         The reason for ignoring the measure
    """

    ap_value: str
    wl_value: str
    evaluation: str
    metrics: Any = None
    ignore: bool = False
    ignore_reason: str = ""


@dataclass
class FieldMeasure:
    """
    Parameters
    ----------
    recommendation: str
        The field level measure recommendation - reduced from all value results
    context: str
        The verbal string describing the field's meaning
    results: List[ValueMeasure]
        The value measure results for all values present in the field
    domain_name: Optional[str] = None
    """

    recommendation: str
    context: str
    results: List[ValueMeasure]
    domain_name: Optional[str] = None
    ignore: Optional[bool] = False


class BaseCustomKnowledge(ABC):
    @abstractmethod
    def run(self, data, knowledge, measures) -> List[ValueKnowledge]:
        return


class BaseCustomMeasure(ABC):
    @abstractmethod
    def reduce_value_measure_results(self, value_measures) -> str:
        return

    @abstractmethod
    def run(self, data=None, knowledge=None, measures=None) -> FieldMeasure:
        return


BaseCustomFeatureType = Union[BaseCustomKnowledge, BaseCustomMeasure]


@dataclass
class SolvedHit:
    feature_vector: Dict[str, str]
    decision: str
    comment: str


@dataclass
class PolicyStep:  # here until it is a solve_hit method argument
    decision: str
    conditions: List[Dict[str, List[str]]]
