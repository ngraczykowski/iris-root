import json
from typing import Any, Dict

from etl_pipeline.service.proto.api.etl_pipeline_pb2 import SUCCESS


class LearningEvent:
    __slots__ = "createDate", "event", "eventNote", "userNote", "userInternalId", "loginName"

    def __init__(self, createDate, event, eventNote, userNote, userInternalId, loginName):
        self.createDate = createDate
        self.event = event
        self.eventNote = eventNote
        self.userNote = userNote
        self.userInternalId = userInternalId
        self.loginName = loginName


class AlertPayload(object):
    __slots__ = "batch_id", "alert_name", "flat_payload", "matches", "status", "data_inputs"

    def __init__(self, batch_id, alert_name, flat_payload, matches, status=SUCCESS):
        self.batch_id = batch_id
        self.alert_name = alert_name
        self.flat_payload = flat_payload
        self.matches = matches
        self.status = status
        self.data_inputs: DataInputRecords = None


class LearningAlertPayload(object):
    __slots__ = (
        "batch_id",
        "alert_name",
        "flat_payload",
        "learning_matches",
        "alert_event_history",
        "status",
    )

    def __init__(
        self,
        batch_id,
        alert_name,
        flat_payload,
        learning_matches,
        alert_event_history,
        status=SUCCESS,
    ):
        self.batch_id = batch_id
        self.alert_name = alert_name
        self.flat_payload = flat_payload
        self.learning_matches = learning_matches
        self.alert_event_history = json.loads(alert_event_history)
        self.status = status


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


class HistoricalDecisionBase:
    __slots__ = (
        "result",
        "watchlist",
        "alerted_party_id",
        "date",
        "status",
        "match",
        "alert",
    )

    def __init__(
        self,
        result: Dict[Any, Any],
        watchlist: str,
        alerted_party_id: str,
        date: int,
        status: str,
        match: Match,
        alert: LearningAlertPayload,
    ):
        self.result = result
        self.match = match
        self.watchlist = watchlist
        self.alerted_party_id = alerted_party_id
        self.date = date
        self.status = status
        self.alert = alert
