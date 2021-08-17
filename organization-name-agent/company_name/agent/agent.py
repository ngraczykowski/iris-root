import itertools
import logging
from typing import Sequence

from agent_base.agent import Agent

from company_name.compare import compare_names
from company_name.names.parse.parse import NameInformation, parse_name
from company_name.solution.scores_reduction import ScoresReduction
from company_name.solution.solution import PairResult, Reason, Result, Solution


class CompanyNameAgent(Agent):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.reduction = ScoresReduction(self.config)

    def _check_pair(
        self, ap_name: NameInformation, mp_name: NameInformation
    ) -> PairResult:
        scores = compare_names(ap_name, mp_name)
        solution, probability = self.reduction.get_reduced_score(scores)

        return PairResult(
            names=(ap_name.source.original, mp_name.source.original),
            solution=solution,
            solution_probability=probability,
        )

    @staticmethod
    def _parse_names(names: Sequence[str]) -> Sequence[NameInformation]:
        return [parse_name(name) for name in set(names) if name]

    def _resolve(self, ap_names: Sequence[str], mp_names: Sequence[str]) -> Result:
        ap_names_parsed = self._parse_names(ap_names)
        mp_names_parsed = self._parse_names(mp_names)

        if not ap_names_parsed or not mp_names_parsed:
            return Result(Solution.NO_DATA)

        values = sorted(
            (
                self._check_pair(ap_name, mp_name)
                for ap_name, mp_name in itertools.product(
                    ap_names_parsed, mp_names_parsed
                )
            ),
            reverse=True,
        )

        return Result(values[0].solution, Reason(values))

    def resolve(self, ap_names: Sequence[str], mp_names: Sequence[str]) -> Result:
        try:
            return self._resolve(ap_names, mp_names)
        except Exception as err:  # noqa
            logging.exception(f"for {ap_names} vs {mp_names}")
            return Result(Solution.AGENT_ERROR)
