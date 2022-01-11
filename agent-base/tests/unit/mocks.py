import itertools

from silenteight.agents.v1.api.exchange.exchange_pb2 import AgentExchangeRequest

from agent_base.agent import Agent


class MockService:
    def __init__(self):
        self.data = {}

    async def start(self):
        pass

    async def stop(self):
        pass


class MockAgent(Agent):
    def resolve(self, *args, **kwargs):
        return "RESOLVED", {"args": list(args)}


class MockAgentDataSource(MockService):
    def set(self, match, feature, args):
        self.data[(match, feature)] = args

    async def request(self, request: AgentExchangeRequest):
        for match, feature in itertools.product(request.matches, request.features):
            if (match, feature) in self.data:
                yield match, feature, self.data[(match, feature)]


class MockPikaConnection(MockService):
    def __init__(self, _messaging_config, _connection_config, callback, max_requests_to_worker):
        super().__init__()
        self.callback = callback
