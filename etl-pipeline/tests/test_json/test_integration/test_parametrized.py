import os
import subprocess
import time

import grpc
import pytest

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import SUCCESS, RunEtlRequest
from etl_pipeline.service.proto.api.etl_pipeline_pb2_grpc import EtlPipelineServiceStub
from tests.test_json.test_integration.test_client import (
    compare_tested_uds_categories_with_reference,
    compare_tested_uds_features_with_reference,
    load_alert,
)


@pytest.fixture(scope="module")
def pipeline_resource(request):
    environment = os.environ.copy()
    service_config = ConsulServiceConfig()
    _ = subprocess.Popen("tests/scripts/start_services.sh", env=environment)
    channel = grpc.insecure_channel(
        f"{service_config.ETL_SERVICE_IP}:{service_config.ETL_SERVICE_PORT}"
    )
    uut = EtlPipelineServiceStub(channel)
    time.sleep(10)
    yield uut
    try:
        os.remove("/tmp/categories.txt")
    except FileNotFoundError:
        pass
    process = subprocess.Popen("tests/scripts/kill_services.sh")
    process.wait()


@pytest.mark.asyncio
@pytest.mark.parametrize(
    ["source_file", "reference_folder"],
    [
        (
            "notebooks/sample/isg_party_in_payload_format_customer_type_individual.json",
            "test_isg_party_in_payload_format_customer_type_individual",
        ),
        (
            "notebooks/sample/isg_party_in_payload_format_customer_type_unknown.json",
            "test_isg_party_in_payload_format_customer_type_unknown",
        ),
        (
            "notebooks/sample/isg_account_in_payload_format_customer_type_individual.json",
            "test_isg_account_in_payload_format_customer_type_individual",
        ),
        (
            "notebooks/sample/isg_account_in_payload_format_customer_type_organization.json",
            "test_isg_account_in_payload_format_customer_type_organization",
        ),
        (
            "notebooks/sample/isg_account_in_payload_format_customer_type_with_no_token.json",
            "test_isg_account_in_payload_format_customer_type_no_token",
        ),
        (
            "notebooks/sample/wm_party_in_payload_format_customer_type_without_carrier_tag.json",
            "test_wm_party_in_payload_format_customer_type_without_carrier_tag",
        ),
    ],
)
async def test_parametrized(source_file, reference_folder, pipeline_resource):
    request_alert = load_alert(source_file)
    for match in request_alert.matches:
        try:
            os.remove(
                f'/tmp/categories_{match.match_name.replace("/", "_")}.json',
            )
        except FileNotFoundError:
            pass
    response = pipeline_resource.RunEtl(RunEtlRequest(alerts=[request_alert]))
    for etl_alert in response.etl_alerts:
        assert etl_alert.etl_status == SUCCESS
    for match in request_alert.matches:
        compare_tested_uds_features_with_reference(
            f'/tmp/features_{match.match_name.replace("/", "_")}.json',
            f'tests/test_json/test_integration/expected_features/{reference_folder}/features_{match.match_name.replace("/", "_")}.json',
        )
        compare_tested_uds_categories_with_reference(
            f'/tmp/categories_{match.match_name.replace("/", "_")}.json',
            f'tests/test_json/test_integration/expected_features/{reference_folder}/categories_{match.match_name.replace("/", "_")}.json',
        )
