import asyncio
import logging
from concurrent import futures
from dataclasses import dataclass
from typing import List

import omegaconf

import etl_pipeline.service.proto.api.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import ConsulServiceConfig, pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from etl_pipeline.service.agent_router import AgentInputCreator
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import FAILURE, SUCCESS, EtlAlert, EtlMatch
from pipelines.ms.ms_pipeline import MSPipeline as WmAddressMSPipeline

cn = pipeline_config.cn


class ParsingError:
    pass


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


pipeline = WmAddressMSPipeline(engine, pipeline_config)


logger = logging.getLogger("main").getChild("servicer")


service_config = ConsulServiceConfig()
try:
    PROCESSES = service_config.PROCESSES
except omegaconf.errors.ConfigAttributeError:
    PROCESSES = 10


class EtlPipelineServiceServicer(object):
    router = AgentInputCreator()  # cannot pass to __init__
    pool = futures.ProcessPoolExecutor(max_workers=service_config.PROCESSES)

    def __init__(self, ssl) -> None:
        EtlPipelineServiceServicer.router.initialize(ssl)
        logger.info("Service started")

    def set_failure_forall(self, alerts):
        return [self._parse_alert(alert, status) for alert, status in alerts]

    async def RunEtl(self, request: etl__pipeline__pb2.RunEtlRequest, context):
        try:
            for alert in request.alerts:
                logger.debug(f"Received {alert.alert_name}")

            alerts_to_parse = [
                AlertPayload(
                    batch_id=alert.batch_id,
                    alert_name=alert.alert_name,
                    flat_payload={
                        key: alert.flat_payload[key] for key in sorted(alert.flat_payload)
                    },
                    matches=[Match(match.match_id, match.match_name) for match in alert.matches],
                )
                for alert in request.alerts
            ]
            logger.debug(f"Number of alerts {len(alerts_to_parse)}")
            etl_alerts = await self.process_request(alerts_to_parse)
        except Exception as e:
            logger.error(f"RunEtl error: {str(e)}")
            etl_alerts = []
        if len(etl_alerts) != len(request.alerts):
            try:
                etl_alerts = self.set_failure_forall(alerts_to_parse)
            except Exception as e:
                logger.error(f"{str(e)} Something wrong with alerts structure. Send empty list")
        return etl__pipeline__pb2.RunEtlResponse(etl_alerts=etl_alerts)

    async def upload_to_data_source(self, alert, record):
        input_match_records, status, error = record
        if status != FAILURE:
            logger.info(f"Batch {alert.batch_id}, Alert {alert.alert_name} parsed successfully")
            for input_match_record in input_match_records:
                logger.info("Trying upload from pipeline to UDS")
                try:
                    await self.add_to_datasource(alert, input_match_record)
                except Exception as e:
                    logger.error(f"Exception {e}")
                    status = FAILURE
                    break
        else:
            logger.info(
                f"Batch {alert.batch_id}, Alert {alert.alert_name} - parsing error: {error}"
            )
        return alert, status

    async def process_request(self, alerts_to_parse):

        # future_payloads = [self.pool.submit(self.parse_alert, alert) for alert in alerts_to_parse]
        # payloads = [future.result() for future in future_payloads]
        # logger.debug(f"Collected parsed payloads {len(alerts_to_parse)}")
        payloads = [self.parse_alert(alert) for alert in alerts_to_parse]  # debugging
        statuses = []
        for alert, record in zip(alerts_to_parse, payloads):
            logger.debug(f"Collected parsed payloads {len(alerts_to_parse)}")
            statuses.append(self.upload_to_data_source(alert, record))
        all_data = await asyncio.gather(*statuses)
        etl_alerts = [self._parse_alert(alert, status) for alert, status in all_data]
        logger.debug(f"ETL results number: {len(etl_alerts)}")
        logger.debug(f"ETL results: {etl_alerts}")
        return etl_alerts

    def parse_alert(self, alert):
        status = SUCCESS
        error = None
        try:
            logger.debug(f"Starting parse for {alert.alert_name}")
            payload = alert.flat_payload
            payload = PayloadLoader().load_payload_from_json(payload)
            payload = {key: payload[key] for key in sorted(payload)}
            payload[cn.MATCH_IDS] = alert.matches
            payload = pipeline.transform_standardized_to_cleansed(payload)
            logger.debug(f"Number of records (input_record vs match pairs): {len(payload)}")
            logger.debug(f"{alert.alert_name} - Transform standardized to cleansed - success")
            payload = pipeline.transform_cleansed_to_application(payload)
            logger.debug(f"{alert.alert_name} - Transform cleansed to application - success")
        except Exception as e:
            error = str(e)
            status = FAILURE
        return [payload, status, error]

    def _parse_alert(self, alert, status):
        etl_alert = EtlAlert(
            batch_id=alert.batch_id,
            alert_name=alert.alert_name,
            etl_status=status,
            etl_matches=[EtlMatch(match_name=str(match)) for match in alert.matches],
        )
        return etl_alert

    @classmethod
    async def add_to_datasource(self, alert, payload):
        await EtlPipelineServiceServicer.router.upload_data_inputs(alert, payload)
