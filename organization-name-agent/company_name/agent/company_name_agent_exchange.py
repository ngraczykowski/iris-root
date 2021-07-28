import asyncio
import collections
import concurrent.futures
import functools
from typing import AsyncGenerator, Callable, Optional, Tuple

from google.protobuf.struct_pb2 import Struct
from silenteight.agents.v1.api.exchange.exchange_pb2 import (
    AgentExchangeRequest,
    AgentExchangeResponse,
    AgentOutput,
)
from silenteight.datasource.api.name.v1.name_pb2 import (
    BatchGetMatchNameInputsRequest,
    BatchGetMatchNameInputsResponse,
    NameFeatureInput,
)

from company_name.base.agent_data_source import (
    AgentDataSource,
    AgentDataSourceException,
)
from company_name.base.agent_exchange import AgentExchange
from company_name.solution.solution import Solution
from company_name.utils.asyncio_utils import awaitify
from company_name.utils.json_encoder import simplify


class CompanyNameAgentExchange(AgentExchange):
    data_source_feature = "features/name"
    response_feature = "features/companyName"
    default_feature_solution = AgentOutput.FeatureSolution(
        solution=Solution.UNEXPECTED_ERROR.value
    )

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.data_source = AgentDataSource(self.config)
        self.pool = None

    async def start(self):
        await asyncio.gather(super().start(), self.data_source.start())
        processes = self.config["agent"].get("processes")
        self.pool = (
            concurrent.futures.ProcessPoolExecutor(processes)
            if processes and processes > 1
            else None
        )

    async def stop(self):
        if self.pool:
            self.pool.shutdown()
        await asyncio.gather(super().stop(), self.data_source.stop())

    async def process(self, request: AgentExchangeRequest) -> AgentExchangeResponse:
        agent_input = self.data_source.request(
            BatchGetMatchNameInputsRequest(
                matches=request.matches,
                features=[self.data_source_feature],  # request.features
            ),
        )
        return await self._resolve_request(request, agent_input)

    async def _resolve_request(
        self,
        request: AgentExchangeRequest,
        request_inputs: AsyncGenerator[BatchGetMatchNameInputsResponse, None],
    ):
        resolved = collections.defaultdict(dict)

        try:
            async for match, task in self._create_name_features_tasks(request_inputs):
                feature = await task
                resolved[match][feature.feature] = feature
        except AgentDataSourceException as err:
            self.logger.warning(repr(err))

        response = AgentExchangeResponse(agent_outputs=[])
        for match in request.matches:
            features = [
                resolved.get(match, {}).get(feature)
                or AgentOutput.Feature(
                    feature=feature, feature_solution=self.default_feature_solution
                )
                for feature in request.features
            ]
            response.agent_outputs.append(AgentOutput(match=match, features=features))

        return response

    async def _create_name_features_tasks(
        self,
        request_inputs: AsyncGenerator[BatchGetMatchNameInputsResponse, None],
    ) -> AsyncGenerator[Tuple[str, asyncio.Future], None]:
        async for request_input in request_inputs:
            for name_input in request_input.name_inputs:
                for name_feature_input in name_input.name_feature_inputs:
                    task = self._create_task(
                        functools.partial(
                            self._resolve_single_request,
                            self.request_callback,
                            name_feature_input,
                            self.response_feature,
                        )
                    )
                    yield name_input.match, task

    def _create_task(self, func: Callable):
        if self.pool:
            return asyncio.get_running_loop().run_in_executor(self.pool, func)
        else:
            return asyncio.create_task(awaitify(func))

    @staticmethod
    def _resolve_single_request(
        request_callback,
        name_feature_input: NameFeatureInput,
        response_feature: Optional[str] = None,
    ) -> Tuple[AgentOutput.Feature]:
        if name_feature_input.alerted_party_type not in (
            NameFeatureInput.EntityType.ORGANIZATION,
            NameFeatureInput.EntityType.ENTITY_TYPE_UNSPECIFIED,
        ):
            return AgentOutput.Feature(
                feature=response_feature or name_feature_input.feature,
                feature_solution=AgentOutput.FeatureSolution(
                    solution=Solution.AGENT_SKIPPED.value
                ),
            )

        ap_names = [name.name for name in name_feature_input.alerted_party_names]
        mp_names = [name.name for name in name_feature_input.watchlist_names]

        result = request_callback(ap_names, mp_names)
        reason = Struct()
        reason.update(simplify(result.reason))
        return AgentOutput.Feature(
            feature=response_feature or name_feature_input.feature,
            feature_solution=AgentOutput.FeatureSolution(
                solution=result.solution.value, reason=reason
            ),
        )
