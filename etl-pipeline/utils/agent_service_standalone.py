from typing import List


from serppythonclient.local.stub import StubProviderFactory
from silenteight.agent.country.v1.api.country_agent_pb2 import \
    CompareCountriesRequest
from silenteight.agent.country.v1.api.country_agent_pb2_grpc import \
    CountryAgentStub
from silenteight.agent.date.v1.api.date_agent_pb2 import RecognizeDatesRequest
from silenteight.agent.date.v1.api.date_agent_pb2_grpc import DateAgentStub
from silenteight.agent.document.v1.api.document_numbers_comparer_agent_pb2 import (
    CompareDocumentNumbersRequest)
from silenteight.agent.document.v1.api.document_numbers_comparer_agent_pb2_grpc import \
    DocumentNumbersComparerAgentStub
from silenteight.agent.gender.v1.api.gender_agent_pb2 import SolveGenderRequest
from silenteight.agent.gender.v1.api.gender_agent_pb2_grpc import \
    GenderAgentStub
from silenteight.agent.geo.v1.api.geo_agent_pb2 import (
    CompareLocationsRequest, CompareLocationsResponse)
from silenteight.agent.geo.v1.api.geo_agent_pb2_grpc import \
    GeoLocationAgentStub
from silenteight.agent.name.v1.api.name_agent_pb2 import (CompareNamesInput,
                                                          CompareNamesRequest,
                                                          WatchlistName)
from silenteight.agent.name.v1.api.name_agent_pb2_grpc import NameAgentStub

import utils.config_service as configservice

name_configuration = configservice.get_agent_configuration("name")
document_configuration = configservice.get_agent_configuration("document")
country_configuration = configservice.get_agent_configuration("country")
gender_configuration = configservice.get_agent_configuration("gender")
geo_configuration = configservice.get_agent_configuration("geo")
date_configuration = configservice.get_agent_configuration("date")


factory = StubProviderFactory.create()

name_agent_stub = factory.get_stub(
    NameAgentStub, "localhost", name_configuration["port"]
)

document_number_stub = factory.get_stub(
    DocumentNumbersComparerAgentStub, "localhost", document_configuration["port"]
)

country_agent_stub = factory.get_stub(
    CountryAgentStub, "localhost", country_configuration["port"]
)
date_agent_stub = factory.get_stub(
    DateAgentStub, "localhost", date_configuration["port"]
)
gender_agent_stub = factory.get_stub(
    GenderAgentStub, "localhost", gender_configuration["port"]
)
geo_agent_stub = factory.get_stub(
    GeoLocationAgentStub, "localhost", geo_configuration["port"]
)


def call_name_agent(
    instance_name: str,
    alerted_names: List[str],
    matched_names: List[str],
    standalone: bool = True,
):

    request = CompareNamesRequest(
        instance_name=instance_name,
        exclude_reason=False,
        inputs=[
            CompareNamesInput(
                alerted_names=alerted_names,
                watchlist_names=[
                    WatchlistName(name=matched_name, type="NAME")
                    for matched_name in matched_names
                ],
            )
        ],
    )

    compared_names = name_agent_stub.CompareNames(request)
    return next(compared_names)


# def call_document_comparer_agent(
#     alerted_documents: List[str],
#     matched_documents: List[str],
#     alerted_party_country_codes: List[str],
#     matched_party_country_codes: List[str],
#     standalone: bool = True,
# ):

#     alerted_documents_list = [
#         CompareDocumentNumbersRequest.DocumentToCompare(number=d)
#         for d in alerted_documents
#     ]
#     matched_documents_list = [
#         CompareDocumentNumbersRequest.DocumentToCompare(number=d)
#         for d in matched_documents
#     ]
#     alerted_party_known_countries = [
#         Facts.CountryFact(code=c) for c in alerted_party_country_codes
#     ]
#     matched_party_known_countries = [
#         Facts.CountryFact(code=c) for c in matched_party_country_codes
#     ]

#     request = CompareDocumentNumbersRequest(
#         alerted_documents=CompareDocumentNumbersRequest.PartyDocuments(
#             documents=alerted_documents_list,
#             known_facts=Facts(countries=alerted_party_known_countries),
#         ),
#         matched_documents=CompareDocumentNumbersRequest.PartyDocuments(
#             documents=matched_documents_list,
#             known_facts=Facts(countries=matched_party_known_countries),
#         ),
#     )

#     return document_number_stub.CompareDocumentNumbers(request)


def call_document_number_agent(
    alerted_documents: List[str], matched_documents: List[str], standalone: bool = True
):
    request = CompareDocumentNumbersRequest(
        alerted_values=alerted_documents,
        matched_values=matched_documents,
    )

    return document_number_stub.CompareDocumentNumbers(request)


def call_country_agent(
    alerted_values: List[str], matched_values: List[str], standalone: bool = True
):

    request = CompareCountriesRequest(
        alerted_values=alerted_values, matched_values=matched_values
    )

    compared_countries = country_agent_stub.CompareCountries(request)
    return compared_countries


def call_date_agent(
    instance_name: str,
    alerted_values: str,
    matched_values: List[str],
    standalone: bool = True,
):

    request = RecognizeDatesRequest(
        instance_name=instance_name,
        alerted_values=alerted_values,
        matched_values=matched_values,
    )

    compared_dates = date_agent_stub.RecognizeDates(request)
    return next(compared_dates)


def call_gender_agent(
    alerted_values: List[str], matched_values: List[str], standalone: bool = True
):

    request = SolveGenderRequest(
        alerted_values=alerted_values, matched_values=matched_values
    )

    compared_genders = gender_agent_stub.SolveGender(request)
    return next(compared_genders)


def call_geo_location_agent(
    alerted_location: str, watchlist_location: str, standalone: bool = True
) -> CompareLocationsResponse:

    request = CompareLocationsRequest(
        alerted_party_location=alerted_location,
        watchlist_party_location=watchlist_location,
    )

    response = geo_agent_stub.CompareLocations(request)
    return response


# below may be refactored as get_agent_response() is more generic
# and agent agnostic. currently done ugly-way to allow running pov
# notebooks without modification

# not sure what this helps - commented out for a moment


# def get_agent_response(host: str,
#                        port: int,
#                        stub,
#                        stub_method: str,
#                        request,
#                        out_format: str = 'dict',
#                        ) -> typing.Union[dict, str, 'protobuf']:
#     '''
#     This function sends request to the agent
#     and returns response as a dict.

#     stub == agent-specific grpc stub.
#     request == google.protobuf.pyext.cpp_message.GeneratedProtocolMessageType,
#     compatible with given agent.
#     out_format in ['dict', 'json', 'protobuf'].
#     '''

#     assert port is not None, 'port is None'

#     factory = StubProviderFactory.create()
#     _agent_stub = factory.get_stub(stub, host, port)

#     # first method from grpc auto-generated objects
#     # method = list(_agent_stub.__dict__.keys())[0]
#     # _agent_stub_response = getattr(_agent_stub, method)(request)
#     _agent_stub_response = getattr(_agent_stub, stub_method)(request)

#     if isinstance(_agent_stub_response,
#                   grpc._channel._MultiThreadedRendezvous):
#         _agent_stub_response = next(_agent_stub_response)

#     if out_format == 'dict':
#         return MessageToDict(_agent_stub_response)
#     elif out_format == 'json':
#         return MessageToJson(_agent_stub_response)
#     elif out_format == 'protobuf':
#         return _agent_stub_response
#     else:
#         raise NotImplementedError(
#             'Only dict/json/protobuf output formats implemented')
