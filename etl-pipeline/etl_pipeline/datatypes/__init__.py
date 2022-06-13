from etl_pipeline.service.proto.api.etl_pipeline_pb2 import SUCCESS


class AlertPayload(object):
    __slots__ = "batch_id", "alert_name", "flat_payload", "matches", "status", "data_inputs"

    def __init__(self, batch_id, alert_name, flat_payload, matches, status=SUCCESS):
        self.batch_id = batch_id
        self.alert_name = alert_name
        self.flat_payload = flat_payload
        self.matches = matches
        self.status = status
        self.data_inputs: DataInputRecords = None


class PipelinedPayload(object):
    __slots__ = "status", "error", "result"

    def __init__(self):
        self.error = None
        self.result = None


class Match(object):
    __slots__ = "match_id", "match_name"

    def __init__(self, match_id, match_name):
        self.match_id = match_id
        self.match_name = match_name


class DataInputRecords:
    __slots__ = "categories", "agent_inputs"

    def __init__(self, categories, agent_inputs):
        self.categories = categories
        self.agent_inputs = agent_inputs
