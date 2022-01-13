from typing import List

from fastapi import APIRouter, Body
from organization_name_knowledge import parse

from company_name.agent.agent import CompanyNameAgent
from company_name.compare import compare_names
from company_name.rest_api.data_models import (
    CompareInput,
    CompareOutput,
    ResolvePairsInput,
    ResolvePairsOutput,
)

router = APIRouter()
TAG = "Organization Name Agent"


@router.post("/compare_names", tags=[TAG], response_model=CompareOutput)
async def compare_endpoint(compare_input: CompareInput = Body(...)) -> CompareOutput:  # noqa: B008
    compare_results_mapping = compare_names(
        parse(compare_input.ap_name), parse(compare_input.wl_name)
    )
    return CompareOutput.from_dict(compare_results_mapping)


@router.post("/resolve_pairs", tags=[TAG], response_model=List[ResolvePairsOutput])
async def resolve_pairs_endpoint(
    pairs: ResolvePairsInput = Body(...),  # noqa: B008
) -> List[ResolvePairsOutput]:
    ap_names = [parse(name) for name in pairs.ap_names]
    wl_names = [parse(name) for name in pairs.wl_names]
    pair_results = CompanyNameAgent().resolve_pairs(ap_names, wl_names)
    return [ResolvePairsOutput.fromPairResult(pair_result) for pair_result in pair_results]
