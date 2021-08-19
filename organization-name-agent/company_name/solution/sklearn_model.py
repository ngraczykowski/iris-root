import pathlib
import pickle
from typing import Mapping

import sklearn  # noqa: F401

from company_name.scores.score import Score


class SklearnModel:
    def __init__(self, path: pathlib.Path):
        with open(path, "rb") as f:
            sklearn_model = pickle.load(f)
        self.lb_classes = sklearn_model["lb_classes"]
        self.clf = sklearn_model["clf"]
        self.imputed_values = sklearn_model["imputed_vals"]

    def __repr__(self):
        return (
            "SkleanModel("
            + ", \n".join(f"{key}={getattr(self, key)!r}" for key in self.__dict__.keys())
            + ")"
        )

    def predict(self, scores: Mapping[str, Score]):
        feature_vector = [
            v.value
            if v.status == Score.ScoreStatus.OK
            else self.imputed_values.get(f"{k}_value", 0)
            for k, v in scores.items()
        ]

        probabilities = self.clf.predict_proba([feature_vector])[0]
        return dict(zip(self.lb_classes, probabilities))
