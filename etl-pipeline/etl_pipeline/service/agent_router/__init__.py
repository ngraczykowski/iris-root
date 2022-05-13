import asyncio
import logging
import os
import time

import grpc
from google.protobuf.any_pb2 import Any
from omegaconf import OmegaConf
from silenteight.datasource.agentinput.api.v1.agent_input_pb2 import AgentInput, FeatureInput
from silenteight.datasource.agentinput.api.v1.agent_input_service_pb2 import (
    BatchCreateAgentInputsRequest,
)
from silenteight.datasource.agentinput.api.v1.agent_input_service_pb2_grpc import (
    AgentInputServiceStub,
)
from silenteight.datasource.categories.api.v2.category_pb2 import Category, CategoryType
from silenteight.datasource.categories.api.v2.category_service_pb2 import (
    BatchCreateCategoriesRequest,
)
from silenteight.datasource.categories.api.v2.category_service_pb2_grpc import CategoryServiceStub
from silenteight.datasource.categories.api.v2.category_value_service_pb2 import (
    BatchCreateCategoryValuesRequest,
    CreateCategoryValuesRequest,
)
from silenteight.datasource.categories.api.v2.category_value_service_pb2_grpc import (
    CategoryValueServiceStub,
)

from etl_pipeline.config import ConsulServiceConfig, pipeline_config
from etl_pipeline.service.agent_router.producers import (  # noqa F401;
    CategoryProducer,
    CountryFeatureInputProducer,
    DateFeatureInputProducer,
    DocumentFeatureInputProducer,
    HistoricalDecisionsFeatureInputProducer,
    HitTypeFeatureInputProducer,
    LocationFeatureInputProducer,
    NameFeatureInputProducer,
)

logger = logging.getLogger("main").getChild("agent_input_creator")
cn = pipeline_config.cn


class AgentInputCreatorError(Exception):
    pass


class AgentInputCreator:
    def __init__(self):
        self.ssl = False

    def initiate_channel(self, endpoint, ssl=False):

        if ssl:
            with open(self.service_config.GRPC_CLIENT_TLS_CA, "rb") as f:
                ca = f.read()
            with open(self.service_config.GRPC_CLIENT_TLS_PRIVATE_KEY, "rb") as f:
                private_key = f.read()
            with open(self.service_config.GRPC_CLIENT_TLS_PUBLIC_KEY_CHAIN, "rb") as f:
                certificate_chain = f.read()
            server_credentials = grpc.ssl_channel_credentials(ca, private_key, certificate_chain)
            channel = grpc.secure_channel(
                endpoint,
                server_credentials,
            )
        else:
            channel = grpc.insecure_channel(endpoint)
        return channel

    def initialize(self, ssl):
        self.ssl = ssl
        self.producers = []
        date_input_config = OmegaConf.load(
            os.path.join(os.environ["CONFIG_APP_DIR"], "agents/features_and_categories.yaml")
        )
        self.category_producers = []
        for feature, params in date_input_config.items():
            if params["feature_type"] == "features":
                self.producers.append(
                    globals()[params["producer_type"]](
                        prefix=params["feature_type"],
                        feature_name=feature,
                        field_maps=params["fields"],
                    )
                )
            elif params["feature_type"] == "categories":
                self.category_producers.append(
                    globals()[params["producer_type"]](
                        prefix=params["feature_type"],
                        feature_name=feature,
                        field_maps=params["fields"],
                    )
                )
        self.date_input_config = date_input_config
        self.service_config = ConsulServiceConfig()
        self.initialize_categories()

    def connect_to_uds(self):
        while True:
            try:
                self.service_config.reload()
                logger.debug(
                    f"Connecting to UDS via {self.service_config.DATA_SOURCE_INPUT_ENDPOINT}"
                )
                channel = self.initiate_channel(
                    self.service_config.DATA_SOURCE_INPUT_ENDPOINT, self.ssl
                )
                self.agent_input_stub = AgentInputServiceStub(channel)
                channel = self.initiate_channel(
                    self.service_config.DATA_SOURCE_INPUT_ENDPOINT, self.ssl
                )
                self.category_input_stub = CategoryValueServiceStub(channel)
                break
            except AttributeError:
                time.sleep(1)

    def initialize_categories(self):
        self.connect_to_uds()
        channel = self.initiate_channel(self.service_config.DATA_SOURCE_INPUT_ENDPOINT, self.ssl)
        categories = []
        for feature, params in self.date_input_config.items():
            if params["feature_type"] == "categories":
                category = Category(
                    name=f"categories/{feature}",
                    display_name=params.display_name,
                    type=getattr(CategoryType, params.category_type),
                    allowed_values=list(params.allowed_values),
                    multi_value=False,
                )
                categories.append(category)
        while True:
            try:

                category_input_stub = CategoryServiceStub(channel)
                category_input_stub.BatchCreateCategories(
                    BatchCreateCategoriesRequest(categories=categories)
                )
                break
            except (grpc.RpcError, AttributeError):
                logger.error("No UDS response. Waiting 1s and try again")
                time.sleep(1)
                self.connect_to_uds()
                channel = self.initiate_channel(
                    self.service_config.DATA_SOURCE_INPUT_ENDPOINT, self.ssl
                )
        logger.info(f"Categories created: {categories}")

    def produce_feature_inputs(self, payload):
        feature_inputs = []
        for producer in self.producers:
            try:
                feature_input = producer.produce_feature_input(payload)
                logger.debug(f"Produced features {str(feature_input)}")
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
            except:
                logger.error(f"Cannot parse features for {producer.feature_name}")
        return feature_inputs

    def produce_categories_inputs(self, payload, match_payload, alert, match_name):
        category_matches = []
        for producer in self.category_producers:
            try:
                category_value = producer.produce_feature_input(
                    payload, match_payload, alert, match_name
                )
                logger.debug(f"Produced features {str(category_value)}")
                category_matches.append(
                    CreateCategoryValuesRequest(
                        category=producer.feature_name, category_values=[category_value]
                    )
                )
            except:
                logger.error(f"Cannot parse features for {producer.feature_name}")
        return category_matches

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

    def produce_batch_create_agent_input_category_request(self, alert, payload):
        all_category_values_requests = []

        for match_id, match in zip(
            payload[cn.MATCH_IDS], payload[cn.WATCHLIST_PARTY][cn.MATCH_RECORDS]
        ):
            category_values_requests = self.produce_categories_inputs(
                payload,
                match_payload=match,
                alert=alert.alert_name,
                match_name=f"{match_id.match_name}",
            )
            if category_values_requests:
                logger.debug(category_values_requests)
                all_category_values_requests.extend(category_values_requests)
        return BatchCreateCategoryValuesRequest(requests=all_category_values_requests)

    async def send_items(self, produce_func, stub_request, alert, payload):
        batch = produce_func(alert, payload)
        response = None
        while True:
            try:
                response = stub_request(batch)
                break
            except (grpc.RpcError) as e:
                if e.code() in [grpc.StatusCode.INVALID_ARGUMENT]:
                    raise AgentInputCreatorError("Invalid argument")
            except AttributeError:
                pass
            logger.warning(
                f"Cannot connect to UDS on {self.service_config.DATA_SOURCE_INPUT_ENDPOINT}"
            )
            time.sleep(5)
            self.initialize_categories()
        logger.debug(f"Response from UDS: {response}")

    async def upload_data_inputs(self, alert, payload):
        logger.debug("Uploading features")
        tasks = [
            self.send_items(
                self.produce_batch_create_agent_input_request,
                self.agent_input_stub.BatchCreateAgentInputs,
                alert,
                payload,
            ),
            self.send_items(
                self.produce_batch_create_agent_input_category_request,
                self.category_input_stub.BatchCreateCategoryValues,
                alert,
                payload,
            ),
        ]
        await asyncio.gather(*tasks)
