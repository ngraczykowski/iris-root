import dataclasses
import pathlib
import pickle
from typing import Any, Sequence, Optional, Mapping, Union, Tuple

import sklearn  # noqa: F401
import yaml

from company_name.solution.solution import Solution
from company_name.scores.score import Score

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
    label: Optional[int] = None

    def __post_init__(self):
        self.solution = Solution(self.solution)
        self.threshold = float(self.threshold)
        if self.label is not None:
            self.label = int(self.label)

    def check(
        self, predicted: Union[float, Sequence[float]]
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
    no_data_values: Mapping[str, float] = None

    def __post_init__(self):
        self.solutions = [ModelSolutionRule(**v) for v in self.solutions]
        self.no_data_values = {
            k: float(v) for k, v in (self.no_data_values.items() or ())
        }

    def check(self, scores: Mapping[str, Score]) -> Optional[SolutionWithProbability]:
        values = [
            v.value
            if v.status == Score.ScoreStatus.OK
            else self.no_data_values.get(k, 0.0)
            for k, v in scores.items()
        ]
        predicted = self.model.predict_proba([values[: len(self.model.coef_[0])]])[0]

        for solution_rule in self.solutions:
            solution = solution_rule.check(predicted)
            if solution is not None:
                return solution


class ScoresReduction:
    reduction_file_name = "reduction-rules.yaml"

    def __init__(self, configuration_dir: pathlib.Path):
        self.rules = self._parse_rules(configuration_dir)

    @classmethod
    def _parse_rules(cls, configuration_dir: pathlib.Path) -> Sequence[ReductionRule]:
        with open(configuration_dir / cls.reduction_file_name) as config_file:
            cfg = yaml.load(config_file, Loader=yaml.FullLoader)

        rules = []
        for rule in cfg["rules"]:
            if "source" in rule:
                with open(configuration_dir / rule["source"], "rb") as model_file:
                    rule["model"] = pickle.load(model_file)
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
