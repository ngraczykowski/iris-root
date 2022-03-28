from concurrent import futures
from dataclasses import dataclass
from typing import List

import etl_pipeline.service.proto.api.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.config import pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from etl_pipeline.logger import get_logger
from etl_pipeline.service.agent_router import AgentInputCreator
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import (
    FAILURE,
    SUCCESS,
    UNKNOWN,
    EtlAlert,
    EtlMatch,
)
from pipelines.ms.ms_pipeline import MSPipeline as WmAddressMSPipeline


class ParsingError:
    pass


logger = get_logger("Servicer")


@dataclass
class AlertPayload:
    batch_id: int
    alert_name: str
    flat_payload: dict
    matches: List[int]


@dataclass
class Match:
    match_id: str
    match_name: str


engine = JsonProcessingEngine(pipeline_config)

router = AgentInputCreator()
logger = get_logger("Message handler")

pipeline = WmAddressMSPipeline(engine, pipeline_config)


class EtlPipelineServiceServicer(object):
    pool = futures.ProcessPoolExecutor(max_workers=1)

    def RunEtl(self, request: etl__pipeline__pb2.RunEtlRequest, context):
        try:
            etl_alerts = self.process_request(request)
        except Exception as e:
            logger.error(f"RunEtl error: {str(e)}")
            etl_alerts = []
        return etl__pipeline__pb2.RunEtlResponse(etl_alerts=etl_alerts)

    def process_request(self, request):
        alerts_to_parse = [
            AlertPayload(
                batch_id=alert.batch_id,
                alert_name=alert.alert_name,
                flat_payload={key: alert.flat_payload[key] for key in sorted(alert.flat_payload)},
                matches=[Match(match.match_id, match.match_name) for match in alert.matches],
            )
            for alert in request.alerts
        ]
        future_payloads = [self.pool.submit(self.parse_alert, alert) for alert in alerts_to_parse]
        payloads = [future.result() for future in future_payloads]
        # payloads = [self.parse_alert(alerts_to_parse[0])]  # debugging
        statuses = []
        for alert, record in zip(alerts_to_parse, payloads):
            input_match_records, status, error = record
            if status != UNKNOWN:
                logger.info(
                    f"Batch {alert.batch_id}, Alert {alert.alert_name} parsed successfully"
                )
                for input_match_record in input_match_records:
                    logger.info("Trying upload from pipeline to UDS")

                    try:
                        self.add_to_datasource(alert, input_match_record)
                    except Exception as e:
                        logger.error(f"Exception {e}")
                        status = FAILURE
                        break
            else:
                logger.info(
                    f"Batch {alert.batch_id}, Alert {alert.alert_name} - parsing error: {error}"
                )
            statuses.append(status)
        etl_alerts = [
            self._parse_alert(alert, status) for alert, status in zip(alerts_to_parse, statuses)
        ]

        return etl_alerts

    def parse_alert(self, alert):
        payload = alert.flat_payload
        payload = PayloadLoader().load_payload_from_json(payload)
        payload = {key: payload[key] for key in sorted(payload)}
        payload[cn.MATCH_IDS] = alert.matches
        status = SUCCESS
        error = None
        try:
            payload = pipeline.transform_standardized_to_cleansed(payload)
            logger.debug("Transform standardized to cleansed - success")
            payload = pipeline.transform_cleansed_to_application(payload)
            logger.debug("Transform cleansed to standardized - success")
        except Exception as e:
            error = str(e)
            status = UNKNOWN
        return [payload, status, error]

    def _parse_alert(self, alert, status):
        etl_alert = EtlAlert(
            batch_id=alert.batch_id,
            alert_name=alert.alert_name,
            etl_status=status,
            etl_matches=[EtlMatch(match_name=str(match)) for match in alert.matches],
        )
        return etl_alert

    def add_to_datasource(self, alert, payload):
        router.upload_data_inputs(alert, payload)
