import asyncio
import logging
from typing import Any, Dict

import grpc
from silenteight.datasource.api.name.v1.name_pb2 import (
    AlertedPartyName,
    BatchGetMatchNameInputsRequest,
    BatchGetMatchNameInputsResponse,
    NameFeatureInput,
    NameInput,
    WatchlistName,
)
from silenteight.datasource.api.name.v1.name_pb2_grpc import (
    NameInputServiceServicer,
    add_NameInputServiceServicer_to_server,
)


class DataSourceMock(NameInputServiceServicer):
    def __init__(self, address, alerts=None, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.server = None
        self.address = address
        self.alerts: Dict[str, Dict[str, Any]] = alerts or {}
        self.logger = logging.getLogger("DataSourceMock")

    async def _prepare_match_name_inputs(self, match, features):
        match_info = self.alerts.get(match, {})
        if match_info.get("sleep"):
            await asyncio.sleep(match_info.get("sleep"))

        return NameInput(
            match=match,
            name_feature_inputs=[
                NameFeatureInput(
                    feature=feature,
                    alerted_party_names=[
                        AlertedPartyName(name=name)
                        for name in match_info.get("alerted_party_names", [])
                    ],
                    watchlist_names=[
                        WatchlistName(name=name)
                        for name in match_info.get("watchlist_names", [])
                    ],
                    alerted_party_type=match_info.get(
                        "alerted_party_type",
                        NameFeatureInput.EntityType.ORGANIZATION,
                    ),
                )
                for feature in features
            ],
        )

    async def BatchGetMatchNameInputs(
        self, request: BatchGetMatchNameInputsRequest, context
    ):
        yield BatchGetMatchNameInputsResponse(
            name_inputs=await asyncio.gather(
                *(
                    self._prepare_match_name_inputs(match, request.features)
                    for match in request.matches
                )
            )
        )

    async def start(self):
        if self.server:
            raise Exception("Server already started")
        self.server = grpc.aio.server()
        add_NameInputServiceServicer_to_server(self, self.server)
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
