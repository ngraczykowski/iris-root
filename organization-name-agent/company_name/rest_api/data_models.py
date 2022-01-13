from typing import List, Mapping, Optional, Tuple

from pydantic import BaseModel

from company_name import Score
from company_name.solution.solution import PairResult


class CompareInput(BaseModel):
    ap_name: str
    wl_name: str


class ScoreRepr(BaseModel):
    status: str
    value: int
    compared: Tuple[Tuple[str, ...], Tuple[str, ...]]

    @classmethod
    def fromScore(cls, score: Score) -> "ScoreRepr":
        return cls(status=score.status.value, value=score.value, compared=score.compared)


class CompareOutput(BaseModel):
    parenthesis_match: ScoreRepr
    abbreviation: ScoreRepr
    fuzzy_on_base: ScoreRepr
    fuzzy_on_suffix: ScoreRepr
    fuzzy: ScoreRepr
    partial_fuzzy: ScoreRepr
    sorted_fuzzy: ScoreRepr
    legal_terms: ScoreRepr
    tokenization: ScoreRepr
    absolute_tokenization: ScoreRepr
    blacklisted: ScoreRepr
    country: ScoreRepr
    phonetics_on_base: ScoreRepr
    phonetics: ScoreRepr
    potential_subsidiary: ScoreRepr
    token_inclusion: ScoreRepr
    first_token: ScoreRepr

    @classmethod
    def from_dict(cls, mapping: Mapping[str, Score]) -> "CompareOutput":
        return cls(**{key: ScoreRepr.fromScore(mapping[key]) for key in mapping})


class ResolvePairsInput(BaseModel):
    ap_names: List[str]
    wl_names: List[str]


class ResolvePairsOutput(BaseModel):
    solution: str
    solution_probability: Optional[float]
    alerted_party_name: str
    watchlist_party_name: str

    @classmethod
    def fromPairResult(cls, pair_result: PairResult) -> "ResolvePairsOutput":
        return cls(
            solution=pair_result.solution.value,
            solution_probability=pair_result.solution_probability,
            alerted_party_name=pair_result.alerted_party_name,
            watchlist_party_name=pair_result.watchlist_party_name,
        )
