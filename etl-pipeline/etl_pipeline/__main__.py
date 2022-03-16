from concurrent import futures
from dataclasses import dataclass
from typing import List

import grpc

import etl_pipeline.service.proto.etl_pipeline_pb2 as etl__pipeline__pb2
from etl_pipeline.config import columns_namespace as cn
from etl_pipeline.config import pipeline_config, service_config
from etl_pipeline.custom.ms.payload_loader import PayloadLoader
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from etl_pipeline.logger import get_logger
from etl_pipeline.service.agent_router import AgentInputCreator
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import FAILURE
from etl_pipeline.service.proto.etl_pipeline_pb2 import SUCCESS, UNKNOWN, EtlAlert, EtlMatch
from pipelines.ms.wm_address_pipeline import MSPipeline

engine = JsonProcessingEngine(pipeline_config)
pipeline = MSPipeline(engine, pipeline_config)
router = AgentInputCreator()
logger = get_logger("ETL PIPELINE")


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


class EtlPipelineServiceServicer(object):
    pool = futures.ProcessPoolExecutor(max_workers=1)

    def RunEtl(self, request: etl__pipeline__pb2.RunEtlRequest, context):
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
        # payloads = [self.parse_alert(alerts_to_parse[0])] debugging
        statuses = []
        logger.info("Payload parsed by pipeline")
        for alert, record in zip(alerts_to_parse, payloads):
            input_match_records, status = record
            for input_match_record in input_match_records:
                logger.info("Trying upload from pipeline to UDS")
                try:
                    self.add_to_datasource(alert, input_match_record)
                except Exception as e:

                    logger.error("Exception :" + str(e))
                    status = FAILURE
                    break
            statuses.append(status)
        etl_alerts = [
            self._parse_alert(alert, payload[1])
            for alert, payload in zip(alerts_to_parse, payloads)
        ]
        response = etl__pipeline__pb2.RunEtlResponse(etl_alerts=etl_alerts)
        return response

    def parse_alert(self, alert):
        payload = alert.flat_payload
        payload = PayloadLoader().load_payload_from_json(payload)
        payload = payload["alertPayload"]

        payload = {key: payload[key] for key in sorted(payload)}

        payload[cn.MATCH_IDS] = alert.matches
        status = SUCCESS
        try:
            payload = pipeline.transform_standardized_to_cleansed(payload)
            payload = pipeline.transform_cleansed_to_application(payload)
        except:
            status = UNKNOWN
        return [payload, status]

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


def add_EtlPipelineServiceServicer_to_server(servicer, server):
    rpc_method_handlers = {
        "RunEtl": grpc.unary_unary_rpc_method_handler(
            servicer.RunEtl,
            request_deserializer=etl__pipeline__pb2.RunEtlRequest.FromString,
            response_serializer=etl__pipeline__pb2.RunEtlResponse.SerializeToString,
        ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
        "silenteight.datascience.etlpipeline.v1.api.EtlPipelineService", rpc_method_handlers
    )
    server.add_generic_rpc_handlers((generic_handler,))


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_EtlPipelineServiceServicer_to_server(EtlPipelineServiceServicer(), server)
    server.add_insecure_port(f"[::]:{service_config.ETL_SERVICE_PORT}")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
