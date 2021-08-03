import dataclasses
import pathlib
from typing import Any, Mapping, Optional, Sequence, Tuple, Union

import sklearn  # noqa: F401
from agent_base.utils import Config

from company_name.scores.score import Score
from company_name.solution.sklearn_model import SklearnModel
from company_name.solution.solution import Solution

SolutionWithProbability = Tuple[Solution, float]


class ReductionRule:
    def check(self, scores: Mapping[str, Score]) -> Optional[SolutionWithProbability]:
        raise NotImplementedError()


@dataclasses.dataclass
class FeatureRule(ReductionRule):
    threshold: float
    feature: str
    solution: Solution
    solution_probability: float = 1

    def __post_init__(self):
        self.solution = Solution(self.solution)
        self.threshold = float(self.threshold)

    def check(self, scores: Mapping[str, Score]) -> Optional[SolutionWithProbability]:
        if (
            not scores.get(self.feature)
            or scores[self.feature].status != Score.ScoreStatus.OK
        ):
            return None
        if scores.get(self.feature).value >= self.threshold:
            return self.solution, self.solution_probability


@dataclasses.dataclass
class ModelSolutionRule:
    solution: Solution
    threshold: float
    label: Optional[str] = None

    def __post_init__(self):
        self.solution = Solution(self.solution)
        self.threshold = float(self.threshold)

    def check(
        self, predicted: Union[float, Mapping[str, float]]
    ) -> Optional[SolutionWithProbability]:
        predicted_value = (
            predicted if isinstance(predicted, float) else predicted[self.label]
        )
        if predicted_value >= self.threshold:
            return self.solution, predicted_value


@dataclasses.dataclass
class ModelRule(ReductionRule):
    solutions: Sequence[ModelSolutionRule]
    source: pathlib.Path
    model: Any = None

    def __post_init__(self):
        self.solutions = [ModelSolutionRule(**v) for v in self.solutions]

    def check(self, scores: Mapping[str, Score]) -> Optional[SolutionWithProbability]:
        predicted = self.model.predict(scores)

        for solution_rule in self.solutions:
            solution = solution_rule.check(predicted)
            if solution is not None:
                return solution


class ScoresReduction:
    reduction_file_name = "reduction-rules.yaml"

    def __init__(self, config: Config):
        self.rules = self._parse_rules(config)

    @classmethod
    def _parse_rules(cls, config: Config) -> Sequence[ReductionRule]:
        cfg = config.load_yaml_config(cls.reduction_file_name, required=True)

        rules = []
        for rule in cfg["rules"]:
            if "source" in rule:
                rule["model"] = SklearnModel(
                    config.get_config_path(rule["source"], required=True)
                )
                rules.append(ModelRule(**rule))
            else:
                rules.append(FeatureRule(**rule))
        return rules

    def get_reduced_score(self, scores: Mapping[str, Score]) -> SolutionWithProbability:
        for rule in self.rules:
            value = rule.check(scores)
            if value is not None:
                return value
        return Solution.INCONCLUSIVE, 0.0
