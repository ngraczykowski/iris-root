import asyncio
import logging
from copy import deepcopy
from typing import Any, Dict

import grpc
from silenteight.datasource.api.hittype.v1.hit_type_pb2 import (
    BatchGetMatchHitTypeInputsRequest,
    BatchGetMatchHitTypeInputsResponse,
    HitTypeFeatureInput,
    HitTypeInput,
    StringList,
    TokensMap,
)
from silenteight.datasource.api.hittype.v1.hit_type_pb2_grpc import (
    HitTypeInputServiceServicer,
    add_HitTypeInputServiceServicer_to_server,
)


def produce_hit_feature_input(feature_name, match):
    match = deepcopy(match)
    for analyzed_token in match.get("triggered_tokens", []):
        map_of_tokens = {}
        for found_token, list_of_fields in match.get("triggered_tokens")[analyzed_token].items():
            tokens = StringList(tokens=list_of_fields)
            map_of_tokens[found_token] = tokens

        match["triggered_tokens"][analyzed_token] = TokensMap(tokens_map=map_of_tokens)

    for category in match.get("trigger_categories", {}):
        match["trigger_categories"][category] = StringList(
            tokens=list(match["trigger_categories"][category])
        )
    return HitTypeFeatureInput(feature=feature_name, **match)


class DataSourceMock(HitTypeInputServiceServicer):
    def __init__(self, address, alerts=None, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.server = None
        self.address = address
        self.alerts: Dict[str, Dict[str, Any]] = alerts or {}
        self.logger = logging.getLogger("DataSourceMock")

    async def _prepare_match_hit_type_inputs(self, match, features):
        match_info = self.alerts.get(match, {})
        if match_info.get("sleep"):
            await asyncio.sleep(match_info.get("sleep"))
        return HitTypeInput(
            match=match,
            hit_type_feature_inputs=[
                produce_hit_feature_input(feature, match_info) for feature in features
            ],
        )

    async def BatchGetMatchHitTypeInputs(self, request: BatchGetMatchHitTypeInputsRequest, context):
        yield BatchGetMatchHitTypeInputsResponse(
            hit_type_inputs=await asyncio.gather(
                *(
                    self._prepare_match_hit_type_inputs(match, request.features)
                    for match in request.matches
                )
            )
        )

    async def start(self):
        if self.server:
            raise Exception("Server already started")
        self.server = grpc.aio.server()
        add_HitTypeInputServiceServicer_to_server(self, self.server)
        self.server.add_insecure_port(self.address)
        await self.server.start()
        asyncio.get_event_loop().create_task(self.server.wait_for_termination())

        self.logger.info("Data source mock server started")
        return self

    async def __aenter__(self):
        await self.start()
        return self

    async def stop(self):
        if self.server:
            await self.server.stop(grace=1)
            self.logger.info("Data source mock server stopped")

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        await self.stop()
