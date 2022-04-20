import logging
import sys
from typing import Callable, Dict, List

from agent_base.agent import Agent

from hit_type.solution.solution import Result, Solution

logger = logging.getLogger(__name__)
c_handler = logging.StreamHandler(sys.stdout)
c_handler.setLevel(logging.DEBUG)


class HitTypeAgent(Agent):
    def resolve(
        self,
        normal_trigger_categories: List[str],
        trigger_categories: Dict[str, List[str]],
        triggered_tokens: Dict[str, Dict[str, List[str]]],
    ) -> Result:
        try:
            logger.debug(
                f"Checking normal trigger categories: {normal_trigger_categories},"
                f"\n trigger categories: {trigger_categories},"
                f"\n trigger tokens: {triggered_tokens}"
            )
            if not normal_trigger_categories or not trigger_categories or not triggered_tokens:
                return Result(Solution.NO_DATA)
            result = self._resolve(normal_trigger_categories, trigger_categories, triggered_tokens)
            logger.debug(f"Solution: {result.solution}")
            return result
        except Exception as err:  # noqa
            logger.exception("UnknownError")
            return Result(Solution.AGENT_ERROR)

    def _resolve(
        self,
        normal_trigger_categories: List[str],
        trigger_categories: Dict[str, List[str]],
        triggered_tokens: Dict[str, Dict[str, List[str]]],
    ) -> Result:
        categories = self.collect_token_trigger_categories(trigger_categories, triggered_tokens)

        solution = self.get_solution(normal_trigger_categories, categories)
        hit_categories = self.collect_hit_categories(normal_trigger_categories, categories)
        return Result(
            solution=solution,
            hit_categories=hit_categories,
            normal_categories=normal_trigger_categories,
        )

    def prepare_trigger_search_dict(
        self, trigger_categories: Dict[str, List[str]]
    ) -> Dict[str, str]:
        search_dict = {}
        for key in trigger_categories.keys():
            for value in trigger_categories[key]:
                search_dict[value] = key
        return search_dict

    def collect_token_trigger_categories(
        self,
        trigger_categories: Dict[str, List[str]],
        triggered_tokens: Dict[str, Dict[str, List[str]]],
    ):

        search_dict = self.prepare_trigger_search_dict(trigger_categories)
        categories = []
        for triggered_tokens in triggered_tokens.values():  # noqa: B020
            mapped_triggers = set()
            for trigger in triggered_tokens.keys():
                if trigger in search_dict.keys():
                    mapped_triggers.add(search_dict.get(trigger))
            # @JGajski: empty triggers -> we don't know what hitted, but hitted
            if len(mapped_triggers) == 0:
                mapped_triggers.add("unknown")
            categories.append(list(mapped_triggers))

        return categories

    def check_category_condition(
        self, condition: Callable, token_categories: List[List[str]]
    ) -> List[bool]:
        return [
            any(condition(category) for category in category_list)
            for category_list in token_categories
        ]

    def get_solution(
        self, normal_categories: List[str], token_categories: List[List[str]]
    ) -> Solution:
        is_normal_mask = self.check_category_condition(
            lambda category: category in normal_categories, token_categories
        )
        is_unknown_mask = self.check_category_condition(
            lambda category: "unknown" in category, token_categories
        )
        if all(is_normal_mask):
            return Solution.NORMAL
        elif any(is_unknown_mask):
            return Solution.INCONCLUSIVE
        elif any(is_normal_mask):
            return Solution.SCATTER
        else:
            return Solution.ENTITY_TYPE_MISMATCH

    def collect_hit_categories(
        self, normal_categories: List[str], token_categories: List[List[str]]
    ) -> List[str]:
        collected = []

        for categories in token_categories:
            match_categories = list(filter(lambda c: c in normal_categories, categories))

            if len(match_categories) > 0:
                collected.extend(match_categories)
            else:
                collected.extend(categories)

        return list(set(collected))
