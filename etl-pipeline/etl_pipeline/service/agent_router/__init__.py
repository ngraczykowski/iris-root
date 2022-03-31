import itertools
from abc import ABC, abstractmethod

import grpc
from google.protobuf.any_pb2 import Any
from silenteight.datasource.agentinput.api.v1.agent_input_pb2 import AgentInput, FeatureInput
from silenteight.datasource.agentinput.api.v1.agent_input_service_pb2 import (
    BatchCreateAgentInputsRequest,
)
from silenteight.datasource.agentinput.api.v1.agent_input_service_pb2_grpc import (
    AgentInputServiceStub,
)
from silenteight.datasource.api.country.v1.country_pb2 import CountryFeatureInput
from silenteight.datasource.api.date.v1.date_pb2 import DateFeatureInput
from silenteight.datasource.api.location.v1.location_pb2 import LocationFeatureInput

from etl_pipeline.config import pipeline_config, service_config
from etl_pipeline.logger import get_logger

logger = get_logger("UPDATE TO DATA SOURCE")
cn = pipeline_config.cn


class AgentInputCreator:
    def __init__(self):
        self.producers = [
            # DobAgentFeatureInputProducer(),
            # GeoResidencyAgentFeatureInputProducer(),
            CountryResidencyAgentFeatureInputProducer()
            # NationalityAgentFeatureInputProducer(),
        ]
        channel = grpc.insecure_channel(service_config.DATA_SOURCE_INPUT_ENDPOINT)
        self.stub = AgentInputServiceStub(channel)

    def produce_feature_inputs(self, payload):
        feature_inputs = []
        for producer in self.producers:
            feature_input = producer.produce_feature_input(payload)
            logger.debug(f"Produced features {feature_input}")
            if isinstance(feature_input, list):

                for input_ in feature_input:
                    target = Any()
                    target.Pack(input_)
                    feature_inputs.append(
                        FeatureInput(feature=producer.feature_name, agent_feature_input=target)
                    )
            else:
                target = Any()
                target.Pack(feature_input)
                feature_inputs.append(
                    FeatureInput(feature=producer.feature_name, agent_feature_input=target)
                )
        return feature_inputs

    def produce_batch_create_agent_input_request(self, alert, payload):
        agent_inputs = []

        for match_id, match in zip(
            payload[cn.MATCH_IDS], payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
        ):
            feature_inputs = self.produce_feature_inputs(match)
            agent_input = AgentInput(
                alert=alert.alert_name,
                match=f"{match_id.match_name}",
                feature_inputs=feature_inputs,
            )
            logger.debug(agent_input)
            agent_inputs.append(agent_input)
        return BatchCreateAgentInputsRequest(agent_inputs=agent_inputs)

    def upload_data_inputs(self, alert, payload):
        batch = self.produce_batch_create_agent_input_request(alert, payload)
        response = self.stub.BatchCreateAgentInputs(batch)
        logger.debug(response)


class Producer(ABC):
    @abstractmethod
    def produce_feature_input(self, payload):
        pass


class DobAgentFeatureInputProducer(Producer):
    feature_name = "features/dateOfBirth"

    def produce_feature_input(self, payload):

        return DateFeatureInput(
            feature=self.feature_name,
            alerted_party_dates=[element for element in payload.get("ap_all_dobs_aggregated", [])],
            watchlist_dates=[element for element in payload.get("wl_all_dobs_aggregated", [])],
            alerted_party_type=DateFeatureInput.EntityType.INDIVIDUAL,
            mode=DateFeatureInput.SeverityMode.NORMAL,
        )


class CountryResidencyAgentFeatureInputProducer(Producer):
    feature_name = "features/residencyCountry"

    def produce_feature_input(self, payload):
        ap_parties = payload.get("ap_all_residencies_aggregated", [])
        wl_parties = payload.get("wl_all_residencies_aggregated", [])
        return CountryFeatureInput(
            feature=self.feature_name,
            alerted_party_countries=ap_parties,
            watchlist_countries=wl_parties,
        )


class GeoResidencyAgentFeatureInputProducer(Producer):
    feature_name = "features/residencyCountry"

    def produce_feature_input(self, payload):
        ap_parties = payload.get("ap_all_residencies_aggregated", [""])
        wl_parties = payload.get("wl_all_residencies_aggregated", [""])
        if not ap_parties:
            ap_parties = [""]
        if not wl_parties:
            wl_parties = [""]
        combinations = list(itertools.product(ap_parties, wl_parties))
        return [
            LocationFeatureInput(
                feature=self.feature_name, alerted_party_location=ap, watchlist_location=wl
            )
            for ap, wl in combinations
        ]


class NationalityAgentFeatureInputProducer(Producer):
    feature_name = "features/geoNationality"

    def produce_feature_input(self, payload):
        ap_parties = payload.get("ap_all_nationalities_aggregated", [])
        wl_parties = payload.get("wl_all_nationalities_aggregated", [])
        combinations = list(itertools.product(ap_parties, wl_parties))

        return [
            LocationFeatureInput(
                feature=self.feature_name, alerted_party_location=ap, watchlist_location=wl
            )
            for ap, wl in combinations
        ]


class EmployerNameAgentFeatureInputProducer(Producer):
    feature_name = "features/employer_name"

    def produce_feature_input(self, payload):
        ap_parties = payload.get("ap_all_employer_aggregated", [])
        wl_parties = payload.get("wl_all_employer_aggregated", [])
        combinations = list(itertools.product(ap_parties, wl_parties))

        return [
            LocationFeatureInput(
                feature=self.feature_name, alerted_party_location=ap, watchlist_location=wl
            )
            for ap, wl in combinations
        ]
