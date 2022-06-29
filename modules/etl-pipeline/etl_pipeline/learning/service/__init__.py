import asyncio
import datetime
import hashlib
import logging
import os
from concurrent import futures
from enum import Enum

import omegaconf

import etl_pipeline.learning.proto.etl_learning_pb2 as etl__learning__pb2
from etl_pipeline.config import ConsulServiceConfig, pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.datatypes import (
    HistoricalDecisionBase,
    LearningAlertPayload,
    Match,
    PipelinedPayload,
)
from etl_pipeline.learning.proto.common_pb2 import Decision, Discriminator, Watchlist
from etl_pipeline.learning.proto.historical_decision_learning_pb2 import (
    Alert,
    AlertedParty,
    HistoricalDecisionLearningStoreExchangeRequest,
)
from etl_pipeline.learning.service.connection import HistoricalDecisionExchange
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import FAILURE
from pipelines.ms.ms_pipeline import MSPipeline

cn = pipeline_config.cn


class ParsingError:
    pass


class DecisionType(str, Enum):
    TRUE_POSITIVE = "TRUE_POSITIVE"
    FALSE_POSITIVE = "FALSE_POSITIVE"
    ANALYST_PENDING = "ANALYST_PENDING"


logger = logging.getLogger("main").getChild("servicer")
pipeline = MSPipeline.build_pipeline(MSPipeline)

ANALYST_DECISION_MAP = {
    "L2 Closed Action - Case Created": DecisionType.TRUE_POSITIVE,
    "L3 Closed Action - Case Created": DecisionType.TRUE_POSITIVE,
    "L1 Closed - No Action False Positive": DecisionType.FALSE_POSITIVE,
    "L1 Closed - No Action Procedural Close": DecisionType.FALSE_POSITIVE,
    "L2 Closed No Action - Could have been closed by L1": DecisionType.FALSE_POSITIVE,
    "L2 Closed No Action - False Positive": DecisionType.FALSE_POSITIVE,
    "L2 Closed No Action - Non GFC Issue Risk Accepted No Action": DecisionType.TRUE_POSITIVE,
    "L3 Closed No Action - Could have been closed by L2": DecisionType.FALSE_POSITIVE,
    "L3 Closed No Action - False Positive": DecisionType.FALSE_POSITIVE,
    "L3 Closed No Action - Non GFC Issue Risk Accepted No Action": DecisionType.TRUE_POSITIVE,
    "L1 Review": DecisionType.ANALYST_PENDING,
    "L1 New": DecisionType.ANALYST_PENDING,
    "L2 Review": DecisionType.ANALYST_PENDING,
    "L2 New": DecisionType.ANALYST_PENDING,
    "L3 Review": DecisionType.ANALYST_PENDING,
    "L3 New": DecisionType.ANALYST_PENDING,
    "L1 Closed Action - Escalate to L2 Potential Match": DecisionType.ANALYST_PENDING,
    "L1 Closed Action - Escalate to L2 Historical/Open Case(s) Exists": DecisionType.ANALYST_PENDING,
    "L1 Closed Action - Escalate to L2 Unable to Clear": DecisionType.ANALYST_PENDING,
    "L2 Closed Action - Escalate to L3 Unable to Clear": DecisionType.ANALYST_PENDING,
    "L2 Closed Action - Escalate to L3 Potential Match": DecisionType.ANALYST_PENDING,
}
service_config = ConsulServiceConfig()
try:
    processes = service_config.processes
except omegaconf.errors.ConfigAttributeError:
    processes = 10


def create_pool():
    logger.debug("Create pool")
    return futures.ProcessPoolExecutor(max_workers=service_config.processes)


class EtlLearningServiceServicer(object):
    pool = create_pool()
    connections = {}

    loggers = {}

    def __init__(self, ssl) -> None:
        self.converter = PayloadToLearningAlertConverter()
        self.exchange = HistoricalDecisionExchange()
        asyncio.get_event_loop().create_task(self.exchange.start())
        logger.info("Learning Service started")

    def set_failure_forall(self, alerts):
        return [self._parse_alert(alert, status) for alert, status in alerts]

    def receive_alert(self, alert: LearningAlertPayload):
        logger.debug(f"Received {alert.alert_name}")
        return LearningAlertPayload(
            batch_id=alert.batch_id,
            alert_name=alert.alert_name,
            flat_payload={key: alert.flat_payload[key] for key in sorted(alert.flat_payload)},
            learning_matches=[
                Match(match.match_id, match.match_name) for match in alert.learning_matches
            ],
            alert_event_history=alert.alert_event_history,
        )

    @classmethod
    def process_request(cls, alert: LearningAlertPayload):

        record = cls.parse_alert(alert)
        requests = cls.send(record, alert)
        return requests, record, alert

    @classmethod
    def parse_alert(cls, alert: LearningAlertPayload):
        cls.get_logger()
        pipeline_result = PipelinedPayload()
        try:
            logger.debug(f"Starting parse for {alert.alert_name}")
            payload = alert.flat_payload
            payload = PayloadLoader().load_payload_from_json(payload)
            payload = {key: payload[key] for key in sorted(payload)}
            payload[cn.MATCH_IDS] = alert.learning_matches
            logger.debug(f"Payload loaded for {alert.alert_name}")
            payload = pipeline.transform_standardized_to_cleansed(payload)
            logger.debug(
                f"Number of records (input_record vs match pairs): {len(payload)} for {alert.alert_name}"
            )
            logger.debug(f"{alert.alert_name} - Transform standardized to cleansed - success")
            pipeline_result.result = pipeline.transform_cleansed_to_application(payload)
            logger.debug(f"{alert.alert_name} - Transform cleansed to application - success")
        except Exception as e:
            pipeline_result.error = str(e)
            pipeline_result.status = FAILURE
        return pipeline_result

    async def RunEtlLearning(self, request: etl__learning__pb2.RunEtlLearningRequest, context):

        try:
            for alert in request.learning_alerts:
                logger.debug(f"Received {alert.alert_name}")

            alerts = [self.receive_alert(alert) for alert in request.learning_alerts]

            logger.debug(f"Number of alerts {len(alerts)}")
            etl_alerts = []
            tasks = []
            for alert in alerts:
                tasks.append(
                    asyncio.get_event_loop().create_task(
                        self.run_task_for_alert(self.pool, self.process_request, alert)
                    )
                )

            pipelined_records = await asyncio.gather(*tasks)
            tasks = []
            etl_alerts = []
            results = []
            for alert in pipelined_records:
                results.append(self.exchange.send_request(alert[0]))
            # results = await asyncio.gather(*tasks)
            for alert, result in zip(alerts, results):
                etl_alerts.append(self._parse_response_alert(alert, result))
            logger.debug(f"Sending: {etl_alerts}")

        except Exception as e:
            logger.error(f"RunEtl error: {str(e)}")
            etl_alerts = []
        if len(etl_alerts) != len(request.learning_alerts):
            try:
                etl_alerts = self.set_failure_forall(request.etl_alerts)
            except Exception as e:
                logger.error(f"{str(e)} Something wrong with alerts structure. Send empty list")
        return etl__learning__pb2.RunEtlLearningResponse(etl_alerts=etl_alerts)

    @classmethod
    def send(cls, record, alert):
        r = PayloadToLearningAlertConverter().convert_to_learning_alert(record, alert)
        return r.SerializeToString()

    async def run_task_for_alert(self, executor, func, args):
        result = await asyncio.get_event_loop().run_in_executor(executor, func, args)
        return result

    def get_connection(self):
        try:
            connection = EtlLearningServiceServicer.connections[os.getpid()]
        except KeyError:
            connection = EtlLearningServiceServicer.connections[
                os.getpid()
            ] = HistoricalDecisionExchange()
            asyncio.get_event_loop().create_task(connection.start())
        return connection

    @classmethod
    def get_logger(cls):
        try:
            logger = cls.loggers[os.getpid()]
        except KeyError:
            logger = cls.loggers[os.getpid()] = logging.getLogger("main").getChild(
                str(os.getpid())
            )
        return logger

    def _parse_response_alert(self, alert: LearningAlertPayload, result):
        if not result:
            alert.status = FAILURE
        etl_alert = etl__learning__pb2.EtlLearningAlert(
            batch_id=alert.batch_id,
            alert_id=alert.alert_event_history["alertEventHistory"]["alertId"],
            etl_status=alert.status,
        )
        return etl_alert


class PayloadToLearningAlertConverter:
    def convert_to_learning_alert(self, result, learning_alert: LearningAlertPayload):
        alerts = []
        for pipeline_result in result.result:
            watchlist = pipeline_result["watchlistParty"]["matchRecords"]["datasetId"]
            alerted_party_id = pipeline_result["watchlistParty"]["matchRecords"][
                "ap_id_tp_marked_agent_input"
            ]

            events = learning_alert.alert_event_history["alertEventHistory"]["eventHistory"]

            date = int(
                max(
                    [
                        datetime.datetime.strptime(
                            learning_event["createDate"], "%Y-%m-%d %H:%M:%S.%f"
                        ).timestamp()
                        for learning_event in events
                        if learning_event["event"] == "Status changed"
                    ]
                )
            )
            decision_base = HistoricalDecisionBase(
                watchlist=watchlist,
                alert=learning_alert,
                match=pipeline_result["match_ids"].match_id,
                alerted_party_id=alerted_party_id,
                result=pipeline_result,
                date=date,
            )

            hist_data = self.prepare_historical_data_for_decision(decision_base)
            decisions = []
            discriminators = []
            decisions.append(
                Decision(id=hist_data["decision_id"], value=hist_data["decision"], created_at=date)
            )
            for discriminator in ["ap_id_tp_marked", "tokens_tp_marked", "ap_name_tp_marked"]:
                if decision_base.result["watchlistParty"]["matchRecords"][
                    f"{discriminator}_agent_input"
                ]:
                    discriminators.append(Discriminator(value=f"mike_{discriminator}"))
            alert = Alert(
                alert_id=decision_base.customer_alert_id,
                match_id=pipeline_result["match_ids"].match_id,
                watchlist=Watchlist(id=watchlist),
                alerted_party=AlertedParty(id=alerted_party_id),
                decisions=decisions,
                discriminators=discriminators,
            )
            alerts.append(alert)
        return HistoricalDecisionLearningStoreExchangeRequest(alerts=alerts)

    def get_decision_value(self, status):
        return ANALYST_DECISION_MAP.get(status, DecisionType.FALSE_POSITIVE)

    def prepare_historical_data_for_decision(self, historical_decision: HistoricalDecisionBase):
        alerted_at = datetime.datetime.strptime(
            historical_decision.result["watchlistParty"]["matchRecords"]["lastMatchedDate"],
            "%m/%d/%y",
        ).strftime("%Y-%m-%d")

        hist_data = {
            "alert_id": historical_decision.alert.alert_name,
            "match_id": historical_decision.match,
            "alerted_party_id": historical_decision.alerted_party_id,
            "watchlist_id": historical_decision.watchlist,
            "watchlist_type": "",
            "decision_id": "",
            "alerted_at": alerted_at,
            "created_at": str(historical_decision.date),
            "decision": self.get_decision_value(historical_decision.status),
        }
        hist_data["decision_id"] = hashlib.sha1(
            "".join(hist_data.values()).encode("utf8")
        ).hexdigest()
        return hist_data
