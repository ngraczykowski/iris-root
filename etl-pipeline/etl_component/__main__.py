from concurrent import futures

import grpc
import proto.etl_pipeline_pb2 as etl__pipeline__pb2


class EtlPipelineServiceServicer(object):
    """Missing associated documentation comment in .proto file."""

    def RunEtl(self, request: etl__pipeline__pb2.RunEtlRequest, context):
        """Missing associated documentation comment in .proto file."""
        print(request)
        alerts = []
        alert: etl__pipeline__pb2.Alert
        for alert in request.alerts:
            alerts.append(self.parse_alert(alert))
        response = etl__pipeline__pb2.RunEtlResponse(etl_alerts=alerts)
        return response

    def parse_alert(self, alert: etl__pipeline__pb2.Alert):
        return etl__pipeline__pb2.EtlAlert(
            batch_id=alert.batch_id,
            alert_name=alert.alert_name,
            etl_status=etl__pipeline__pb2.SUCCESS,
            etl_matches=[
                etl__pipeline__pb2.EtlMatch(match_name=match.match_name) for match in alert.matches
            ],
        )


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
    server.add_insecure_port("[::]:50051")
    server.start()
    server.wait_for_termination()


serve()
