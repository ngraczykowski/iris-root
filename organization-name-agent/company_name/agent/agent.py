import itertools
import logging
import pathlib
from typing import Optional, Sequence

from agent_base.agent import Agent
from organization_name_knowledge import KnowledgeBase, NameInformation, parse

from company_name.compare import compare_names
from company_name.solution.name_preconditions import NamePreconditions
from company_name.solution.scores_reduction import ScoresReduction
from company_name.solution.solution import PairResult, Reason, Result, Solution
from company_name.utils.names_abbreviations_filtering import remove_redundant_abbreviations

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler()
c_handler.setLevel(logging.DEBUG)


class CompanyNameAgent(Agent):
    def __init__(self, *args, additional_knowledge_dir: Optional[pathlib.Path] = None, **kwargs):
        super().__init__(*args, **kwargs)
        KnowledgeBase.set_additional_source_paths(
            dirs=(additional_knowledge_dir,) if additional_knowledge_dir else ()
        )
        self.reduction = ScoresReduction(self.config)
        self.name_preconditions = NamePreconditions(self.config)

    def _check_pair(
        self,
        ap_name: NameInformation,
        mp_name: NameInformation,
        min_solution: Solution,
    ) -> PairResult:
        scores = compare_names(ap_name, mp_name)
        solution, probability = max(self.reduction.get_reduced_score(scores), (min_solution, 0))

        return PairResult(
            alerted_party_name=ap_name.source.original,
            watchlist_party_name=mp_name.source.original,
            solution=solution,
            solution_probability=probability,
        )

    def resolve_pairs(
        self, ap_names: Sequence[NameInformation], mp_names: Sequence[NameInformation]
    ) -> Sequence[PairResult]:
        min_solutions = {
            name: (
                Solution.NO_MATCH
                if self.name_preconditions.preconditions_met(name)
                else Solution.INCONCLUSIVE
            )
            for name in (*ap_names, *mp_names)
        }

        return [
            self._check_pair(ap_name, mp_name, max(min_solutions[ap_name], min_solutions[mp_name]))
            for ap_name, mp_name in itertools.product(ap_names, mp_names)
        ]

    @staticmethod
    def _parse_names(names: Sequence[str]) -> Sequence[NameInformation]:
        return [parse(name) for name in set(names) if name]

    @classmethod
    def _prepare_names(cls, names: Sequence[str]) -> Sequence[NameInformation]:
        names_parsed = cls._parse_names(names)
        return remove_redundant_abbreviations(names_parsed)

    def _resolve(self, ap_names: Sequence[str], mp_names: Sequence[str]) -> Result:
        ap_names_parsed = self._prepare_names(ap_names)
        mp_names_parsed = self._prepare_names(mp_names)

        if not ap_names_parsed or not mp_names_parsed:
            return Result(Solution.NO_DATA)

        pair_results = sorted(self.resolve_pairs(ap_names_parsed, mp_names_parsed), reverse=True)

        return Result(pair_results[0].solution, Reason(pair_results))

    def resolve(self, ap_names: Sequence[str], mp_names: Sequence[str]) -> Result:
        try:
            logger.info(f"Checking {ap_names} vs {mp_names}")
            result = self._resolve(ap_names, mp_names)
            logger.info(result)
            return result
        except Exception as err:  # noqa
            logger.exception(f"for {ap_names} vs {mp_names}")
            return Result(Solution.AGENT_ERROR)
