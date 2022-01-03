from fastapi import APIRouter, Path

from organization_name_knowledge import parse, parse_freetext

router = APIRouter()
TAG = "Organization Name Knowledge"


@router.get("/parse/{name}", tags=[TAG])
async def parse_endpoint(name: str = Path(...)):  # noqa: B008
    name_information = parse(name=name)
    return name_information.name()


@router.get("/parse_freetext/{text}", tags=[TAG])
async def parse_freetext_endpoint(text: str = Path(...)):  # noqa: B008
    found_name_information_collection = parse_freetext(freetext=text)
    return [name_information.name() for name_information in found_name_information_collection]
