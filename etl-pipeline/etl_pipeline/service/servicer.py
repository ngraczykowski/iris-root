import asyncio
import logging
import os
from concurrent import futures
from typing import List

import omegaconf

import etl_pipeline.service.proto.api.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import ConsulServiceConfig, pipeline_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.datatypes import AlertPayload, DataInputRecords, Match, PipelinedPayload
from etl_pipeline.logger import get_logger
from etl_pipeline.service.agent_router import AgentInputCreator
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import FAILURE, SUCCESS, EtlAlert, EtlMatch
from pipelines.ms.ms_pipeline import MSPipeline

cn = pipeline_config.cn


class ParsingError:
    pass


pipeline = MSPipeline.build_pipeline(MSPipeline)


logger = logging.getLogger("main").getChild("servicer")

service_config = ConsulServiceConfig()
try:
    processes = service_config.processes
except omegaconf.errors.ConfigAttributeError:
    processes = 10


class EtlPipelineServiceServicer:
    routers = {}
    loggers = {}
    pool = futures.ProcessPoolExecutor(max_workers=service_config.processes)

    def __init__(self, ssl) -> None:
        self.ssl = ssl
        AgentInputCreator().initialize(ssl)
        logger.info("Service started")

    def set_failure_forall(self, alerts):
        return [self._parse_alert(alert, status) for alert, status in alerts]

    def receive_alert(self, alert: AlertPayload):
        logger.debug(f"Received {alert.alert_name}")
        return AlertPayload(
            batch_id=alert.batch_id,
            alert_name=alert.alert_name,
            flat_payload={key: alert.flat_payload[key] for key in sorted(alert.flat_payload)},
            matches=[Match(match.match_id, match.match_name) for match in alert.matches],
            status=SUCCESS,
        )

    async def RunEtl(self, request: etl__pipeline__pb2.RunEtlRequest, context):
        try:
            for alert in request.alerts:
                logger.debug(f"Received {alert.alert_name}")
            alerts = [self.receive_alert(alert) for alert in request.alerts]
            logger.debug(f"Number of alerts {len(alerts)}")
            etl_alerts = []
            tasks = []
            self.process_request(alerts[0])
            for alert in alerts:
                tasks.append(
                    asyncio.get_event_loop().create_task(
                        self.run_task_for_alert(self.pool, self.process_request, alert)
                    )
                )

            etl_alerts = await asyncio.gather(*tasks)

            tasks = []
            for n in range(0, len(etl_alerts), service_config.uds_batch_size):
                etl_alerts_batch = etl_alerts[n : (n + 1) * service_config.uds_batch_size]
                tasks.append(
                    asyncio.get_event_loop().create_task(
                        self.run_task_for_alert(
                            self.pool, self.add_to_datasource, etl_alerts_batch
                        )
                    )
                )

            etl_alerts_batches = await asyncio.gather(*tasks)
            etl_alerts = [
                self._parse_alert(alert) for batch in etl_alerts_batches for alert in batch
            ]
        except Exception as e:
            logger.error(f"RunEtl error: {str(e)}")
            etl_alerts = []
        if len(etl_alerts) != len(request.alerts):
            try:
                etl_alerts = self.set_failure_forall(request.alerts)
            except Exception as e:
                logger.error(f"{str(e)} Something wrong with alerts structure. Send empty list")
        return etl__pipeline__pb2.RunEtlResponse(etl_alerts=etl_alerts)

    async def run_task_for_alert(self, executor, func, args):
        result = await asyncio.get_event_loop().run_in_executor(executor, func, args)
        return result

    def get_router(self):
        try:
            router = EtlPipelineServiceServicer.routers[os.getpid()]
        except KeyError:
            router = EtlPipelineServiceServicer.routers[os.getpid()] = AgentInputCreator()
            router.initialize(self.ssl)
        return router

    def prepare_inputs(self, alert: AlertPayload, record: PipelinedPayload):
        router = self.get_router()
        logger = self.get_logger()
        category_batches = []
        agent_input_batches = []
        if alert.status != FAILURE:
            logger.info(f"Batch {alert.batch_id}, Alert {alert.alert_name} parsed successfully")
            for input_match_record in record.result:
                logger.info("Trying upload from pipeline to UDS")
                try:
                    agent_input_batches.append(
                        router.produce_batch_create_agent_input_request(alert, input_match_record)
                    )
                    category_batches.append(
                        router.produce_batch_create_agent_input_category_request(
                            alert, input_match_record
                        )
                    )
                except Exception as e:
                    logger.error(f"Exception {e}")
                    category_batches = []
                    agent_input_batches = []
                    alert.status = FAILURE
                    break
        else:
            logger.info(
                f"Batch {alert.batch_id}, Alert {alert.alert_name} - parsing error: {record.error}"
            )
        alert.data_inputs = DataInputRecords(category_batches, agent_input_batches)
        return alert

    def process_request(self, alert):
        record = self.parse_alert(alert)
        result = self.prepare_inputs(alert, record)
        return result

    def get_logger(self):
        try:
            logger = EtlPipelineServiceServicer.loggers[os.getpid()]
        except KeyError:
            logger = EtlPipelineServiceServicer.loggers[os.getpid()] = get_logger(
                "main", "ms_pipeline.log"
            )
        return logger

    def parse_alert(self, alert: AlertPayload):
        pipeline_result = PipelinedPayload()
        logger = self.get_logger()
        try:
            logger.debug(f"Starting parse for {alert.alert_name}")
            payload = alert.flat_payload
            payload = PayloadLoader().load_payload_from_json(payload)
            payload = {key: payload[key] for key in sorted(payload)}
            payload[cn.MATCH_IDS] = alert.matches
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

    def _parse_alert(self, alert):
        etl_alert = EtlAlert(
            batch_id=alert.batch_id,
            alert_name=alert.alert_name,
            etl_status=alert.status,
            etl_matches=[EtlMatch(match_name=str(match)) for match in alert.matches],
        )
        return etl_alert

    def add_to_datasource(self, alerts: List[AlertPayload]):
        _ = get_logger("main", "ms_pipeline.log")
        router = self.get_router()

        category_batches = []
        agent_input_batches = []

        for alert in alerts:
            category_batches.extend(
                [batch for match in alert.data_inputs.categories for batch in match]
            )
            agent_input_batches.extend(
                [batch for match in alert.data_inputs.agent_inputs for batch in match]
            )

        try:
            [
                router.send_items(category_batches, "categories"),
                router.send_items(
                    agent_input_batches,
                    "features",
                ),
            ]
        except:
            for alert in alerts:
                alert.status = FAILURE

        return alerts
