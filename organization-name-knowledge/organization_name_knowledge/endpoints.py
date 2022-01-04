from fastapi import APIRouter, Path

from organization_name_knowledge.api import parse, parse_freetext
from organization_name_knowledge.data_models import OriginalNameInformation

router = APIRouter()
TAG = "Organization Name Knowledge"


@router.get("/parse/{name}", tags=[TAG])
async def parse_endpoint(name: str = Path(...)):  # noqa: B008
    name_information = parse(name=name)
    return OriginalNameInformation.from_NameInformation(name_information)


@router.get("/parse_freetext/{text}", tags=[TAG])
async def parse_freetext_endpoint(text: str = Path(...)):  # noqa: B008
    found_name_information_list = parse_freetext(freetext=text)
    return [
        OriginalNameInformation.from_NameInformation(name_information)
        for name_information in found_name_information_list
    ]
