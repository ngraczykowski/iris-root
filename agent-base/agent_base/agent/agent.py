from agent_base.utils import Config


class Agent:
    def __init__(self, config: Config = None):
        self.config = config or Config()

    def resolve(self, *args, **kwargs):
        raise NotImplementedError()
