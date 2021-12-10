from typing import List

from serppythonclient.stub import StubProviderFactory
from silenteight.agent.country.v1.api.country_agent_pb2 import CompareCountriesRequest
from silenteight.agent.country.v1.api.country_agent_pb2_grpc import CountryAgentStub
from silenteight.agent.date.v1.api.date_agent_pb2 import RecognizeDatesRequest
from silenteight.agent.date.v1.api.date_agent_pb2_grpc import DateAgentStub
from silenteight.agent.document.v1.api.document_agent_pb2 import (
    CompareDocumentNumbersRequest,
    Facts,
    SolveDocNoRequest,
)
from silenteight.agent.document.v1.api.document_agent_pb2_grpc import DocumentAgentStub
from silenteight.agent.gender.v1.api.gender_agent_pb2 import SolveGenderRequest
from silenteight.agent.gender.v1.api.gender_agent_pb2_grpc import GenderAgentStub
from silenteight.agent.geo.v1.api.geo_agent_pb2 import (
    CompareLocationsRequest,
    CompareLocationsResponse,
)
from silenteight.agent.geo.v1.api.geo_agent_pb2_grpc import GeoLocationAgentStub
from silenteight.agent.name.v1.api.name_agent_pb2 import (
    CompareNamesInput,
    CompareNamesRequest,
    WatchlistName,
)
from silenteight.agent.name.v1.api.name_agent_pb2_grpc import NameAgentStub

import utils.config_service as configservice

serp_client_properties = configservice.get_serp_client_config()
factory = StubProviderFactory.create(serp_client_properties)
name_agent_stub = factory.get_stub(NameAgentStub, "grpc-name-agent")
document_comparer_stub = factory.get_stub(DocumentAgentStub, "grpc-document-comparer-agent")
document_number_stub = factory.get_stub(DocumentAgentStub, "grpc-document-number-agent")
country_agent_stub = factory.get_stub(CountryAgentStub, "grpc-country-agent")
date_agent_stub = factory.get_stub(DateAgentStub, "grpc-date-agent")
gender_agent_stub = factory.get_stub(GenderAgentStub, "grpc-gender-agent")
geo_agent_stub = factory.get_stub(GeoLocationAgentStub, "grpc-geo-agent")


def call_name_agent(instance_name: str, alerted_names: List[str], matched_names: List[str]):
    request = CompareNamesRequest(
        instance_name=instance_name,
        exclude_reason=False,
        inputs=[
            CompareNamesInput(
                alerted_names=alerted_names,
                watchlist_names=[WatchlistName(name=matched_name, type="NAME") for matched_name in matched_names],
            )
        ],
    )

    compared_names = name_agent_stub.CompareNames(request)
    return next(compared_names)


def call_document_comparer_agent(
    alerted_documents: List[str],
    matched_documents: List[str],
    alerted_party_country_codes: List[str],
    matched_party_country_codes: List[str],
):
    alerted_documents_list = [CompareDocumentNumbersRequest.DocumentToCompare(number=d) for d in alerted_documents]
    matched_documents_list = [CompareDocumentNumbersRequest.DocumentToCompare(number=d) for d in matched_documents]
    alerted_party_known_countries = [Facts.CountryFact(code=c) for c in alerted_party_country_codes]
    matched_party_known_countries = [Facts.CountryFact(code=c) for c in matched_party_country_codes]

    request = CompareDocumentNumbersRequest(
        alerted_documents=CompareDocumentNumbersRequest.PartyDocuments(
            documents=alerted_documents_list,
            known_facts=Facts(countries=alerted_party_known_countries),
        ),
        matched_documents=CompareDocumentNumbersRequest.PartyDocuments(
            documents=matched_documents_list,
            known_facts=Facts(countries=matched_party_known_countries),
        ),
    )

    return document_comparer_stub.CompareDocumentNumbers(request)


def call_document_number_agent(alerted_documents: List[str], matched_documents: List[str]):
    request = SolveDocNoRequest(
        alerted_values=alerted_documents,
        matched_values=matched_documents,
    )
    return document_number_stub.SolveDocNo(request)


def call_country_agent(alerted_values: List[str], matched_values: List[str]):
    request = CompareCountriesRequest(alerted_values=alerted_values, matched_values=matched_values)

    compared_countries = country_agent_stub.CompareCountries(request)
    return compared_countries


def call_date_agent(instance_name: str, alerted_values: str, matched_values: List[str]):
    request = RecognizeDatesRequest(
        instance_name=instance_name,
        alerted_values=alerted_values,
        matched_values=matched_values,
    )

    compared_dates = date_agent_stub.RecognizeDates(request)
    return next(compared_dates)


def call_gender_agent(alerted_values: List[str], matched_values: List[str]):
    request = SolveGenderRequest(alerted_values=alerted_values, matched_values=matched_values)

    compared_genders = gender_agent_stub.SolveGender(request)
    return next(compared_genders)


def call_geo_location_agent(alerted_location: str, watchlist_location: str) -> CompareLocationsResponse:
    request = CompareLocationsRequest(
        alerted_party_location=alerted_location,
        watchlist_party_location=watchlist_location,
    )
    response = geo_agent_stub.CompareLocations(request)
    return response
