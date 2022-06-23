import json
import os
import re
import subprocess
import time
from copy import deepcopy
from glob import glob

import grpc
import pytest
from aiounittest import AsyncTestCase

from etl_pipeline.config import ConsulServiceConfig
from etl_pipeline.service.proto.api.etl_pipeline_pb2 import (
    FAILURE,
    SUCCESS,
    Alert,
    Match,
    RunEtlRequest,
)
from etl_pipeline.service.proto.api.etl_pipeline_pb2_grpc import EtlPipelineServiceStub


def sort_geo_feature(tested_item, reference_item):
    if tested_item.get("alertedPartyLocation", None):
        tested_item["alertedPartyLocation"] = set(tested_item["alertedPartyLocation"].split())
        reference_item["alertedPartyLocation"] = set(
            reference_item["alertedPartyLocation"].split()
        )
    if tested_item.get("watchlistLocation", None):
        tested_item["watchlistLocation"] = set(tested_item["watchlistLocation"].split())
        reference_item["watchlistLocation"] = set(reference_item["watchlistLocation"].split())


def sort_hit_feature(i, j):
    for key in i["triggeredTokens"]:
        for token in i["triggeredTokens"][key]:
            i["triggeredTokens"][key][token] = sorted(i["triggeredTokens"][key][token])
            j["triggeredTokens"][key][token] = sorted(j["triggeredTokens"][key][token])
    for key in i["triggerCategories"]:
        for token in i["triggerCategories"][key]:
            i["triggerCategories"][key][token] = sorted(i["triggerCategories"][key][token])
            j["triggerCategories"][key][token] = sorted(j["triggerCategories"][key][token])


def compare_tested_uds_features_with_reference(tested_file, reference_file):  # noqa: C901
    with open(tested_file, "r") as f1, open(reference_file, "r") as f2:
        tested = json.load(f1)
        reference = json.load(f2)
        for tested_item, reference_item in zip(tested, reference):
            if tested_item["feature"] in [
                "features/name",
                "features/companyName",
                "features/employerName",
            ]:
                if tested_item.get("alertedPartyNames", None):
                    tested_item["alertedPartyNames"] = sorted(
                        tested_item["alertedPartyNames"], key=lambda x: x["name"]
                    )
                    reference_item["alertedPartyNames"] = sorted(
                        reference_item["alertedPartyNames"], key=lambda x: x["name"]
                    )

                if tested_item.get("watchlistNames", None):

                    tested_item["watchlistNames"] = sorted(
                        tested_item["watchlistNames"], key=lambda x: x["name"]
                    )
                    reference_item["watchlistNames"] = sorted(
                        reference_item["watchlistNames"], key=lambda x: x["name"]
                    )
            if tested_item["feature"] == "features/dateOfBirth":
                if tested_item.get("alertedPartyDates", None):
                    tested_item["alertedPartyDates"] = sorted(tested_item["alertedPartyDates"])
                    reference_item["alertedPartyDates"] = sorted(
                        reference_item["alertedPartyDates"]
                    )
                if tested_item.get("watchlistDates", None):
                    tested_item["watchlistDates"] = sorted(tested_item["watchlistDates"])
                    reference_item["watchlistDates"] = sorted(reference_item["watchlistDates"])
            if tested_item["feature"] == "features/document":
                if tested_item.get("alertedPartyDocuments", None):
                    tested_item["alertedPartyDocuments"] = sorted(
                        tested_item["alertedPartyDocuments"]
                    )
                    reference_item["alertedPartyDocuments"] = sorted(
                        reference_item["alertedPartyDocuments"]
                    )
            if "geo" in tested_item["feature"]:
                sort_geo_feature(tested_item, reference_item)
            if "hitType" in tested_item["feature"]:
                sort_hit_feature(tested_item, reference_item)
            assert tested_item == reference_item


def compare_tested_uds_categories_with_reference(tested_file, reference_file):  # noqa: C901
    with open(tested_file, "r") as f1, open(reference_file, "r") as f2:
        tested = sorted(
            [json.loads(line) for line in f1.readlines()],
            key=lambda x: x.get("singleValue") or x.get("category"),
        )
        reference = sorted(
            [json.loads(line) for line in f2.readlines()],
            key=lambda x: x.get("singleValue") or x.get("category"),
        )
        assert tested == reference
    os.remove(tested_file)


def load_alert(filepath: str = "notebooks/sample/wm_address_in_payload_format.json"):
    with open(filepath, "r") as f:
        text = json.load(f)
        ids = [
            text[i]
            for key in text
            for i in re.compile(r".*matchRecords\[\d+\].matchId.*").findall(key)
            if i
        ]
        matches_ids = [
            Match(match_id=i, match_name=f"alerts/2/matches/{num}") for num, i in enumerate(ids)
        ]
        alert = Alert(batch_id="1", alert_name="alerts/2", matches=matches_ids)
        for key, value in text.items():
            alert.flat_payload[str(key)] = "null" if value is None else str(value)
    return alert


class BaseGrpcTestCase:
    @pytest.mark.asyncio
    class TestGrpcServer(AsyncTestCase):
        TIMEOUT = 10

        def setUp(self) -> None:

            category_files = glob("/tmp/categories_*")
            for i in category_files:
                try:
                    os.remove(i)
                except FileNotFoundError:
                    pass
            category_files = glob("/tmp/features*")
            for i in category_files:
                try:
                    os.remove(i)
                except FileNotFoundError:
                    pass

        @classmethod
        def tearDownClass(cls):
            try:
                os.remove("/tmp/categories.txt")
            except FileNotFoundError:
                pass
            process = subprocess.Popen("tests/scripts/kill_services.sh")
            process.wait()

        @pytest.mark.asyncio
        async def test_ok_flow(self):
            request_alert = load_alert()
            for match in request_alert.matches:
                try:
                    os.remove(
                        f'/tmp/categories_{match.match_name.replace("/", "_")}.json',
                    )
                except FileNotFoundError:
                    pass
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[request_alert]))

            for etl_alert in response.etl_alerts:
                assert etl_alert.etl_status == SUCCESS
            for match in request_alert.matches:
                compare_tested_uds_features_with_reference(
                    f'/tmp/features_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_ok_flow_features/features_{match.match_name.replace("/", "_")}.json',
                )

                compare_tested_uds_categories_with_reference(
                    f'/tmp/categories_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_ok_flow_features/categories_{match.match_name.replace("/", "_")}.json',
                )

        @pytest.mark.asyncio
        async def test_failures_flow(self):
            alert = load_alert()
            alerts = [deepcopy(alert) for _ in range(10)]

            alerts[0].matches[0].match_name = "test"
            alerts[9].matches[0].match_name = "test"
            alerts[8].flat_payload["alertedParty.test"] = "test"
            counter = 0
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=alerts))
            assert len(alerts) == len(response.etl_alerts)
            for alert in response.etl_alerts:
                try:
                    assert alert.etl_status == SUCCESS
                except AssertionError:
                    assert alert.etl_status == FAILURE
                    counter += 1
            assert counter == 10

        @pytest.mark.asyncio
        def test_cross_input_match_records(self):
            alert = load_alert(
                "notebooks/sample/wm_address_in_payload_format_2_input_3_match_records.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))

            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_empty_supplemental_info(self):
            alert = load_alert("notebooks/sample/wm_party_payload_without_supplemental_info.json")
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))

            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_partial_supplemental_info(self):
            alert = load_alert(
                "notebooks/sample/wm_party_payload_with_partial_supplemental_info.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))

            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_wm_party(self):
            alert = load_alert("notebooks/sample/wm_party_in_payload_format.json")
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))
            for alert in response.etl_alerts:
                assert alert.etl_status == SUCCESS

        @pytest.mark.asyncio
        def test_wrong_alert_name(self):
            alert = load_alert()
            alert.matches[0].match_name = "2"
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[alert]))
            assert response.etl_alerts[0].etl_status == FAILURE

        @pytest.mark.asyncio
        def test_empty_flow(self):
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[]))
            assert [i for i in response.etl_alerts] == []

        @pytest.mark.asyncio
        async def test_ok_for_big_payload(self):
            request_alert = load_alert("notebooks/sample/big_fat_flat_payload.json")
            for match in request_alert.matches:
                try:
                    os.remove(
                        f'/tmp/categories_{match.match_name.replace("/", "_")}.json',
                    )
                except FileNotFoundError:
                    pass
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[request_alert]))

            for etl_alert in response.etl_alerts:
                assert etl_alert.etl_status == SUCCESS
            for match in request_alert.matches:
                compare_tested_uds_features_with_reference(
                    f'/tmp/features_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_ok_for_big_payload/features_{match.match_name.replace("/", "_")}.json',
                )
                compare_tested_uds_categories_with_reference(
                    f'/tmp/categories_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_ok_for_big_payload/categories_{match.match_name.replace("/", "_")}.json',
                )

        @pytest.mark.asyncio
        async def test_wm_party_in_payload_format_customer_type(self):
            request_alert = load_alert(
                "notebooks/sample/wm_party_in_payload_format_customer_type.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[request_alert]))
            for etl_alert in response.etl_alerts:
                assert etl_alert.etl_status == SUCCESS
            for match in request_alert.matches:
                compare_tested_uds_features_with_reference(
                    f'/tmp/features_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_wm_party_in_payload_format_customer_type/features_{match.match_name.replace("/", "_")}.json',
                )

        @pytest.mark.asyncio
        async def test_wm_party_in_payload_format_customer_type_individual(self):
            request_alert = load_alert(
                "notebooks/sample/wm_party_in_payload_format_customer_type_individual.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[request_alert]))
            for etl_alert in response.etl_alerts:
                assert etl_alert.etl_status == SUCCESS
            for match in request_alert.matches:
                compare_tested_uds_features_with_reference(
                    f'/tmp/features_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_wm_party_in_payload_format_customer_type_individual/features_{match.match_name.replace("/", "_")}.json',
                )

        @pytest.mark.asyncio
        async def test_isg_party_in_payload_format_customer_type_company(self):
            request_alert = load_alert(
                "notebooks/sample/isg_party_in_payload_format_customer_type_company.json"
            )
            response = getattr(type(self), "stub").RunEtl(RunEtlRequest(alerts=[request_alert]))
            for etl_alert in response.etl_alerts:
                assert etl_alert.etl_status == SUCCESS
            for match in request_alert.matches:
                compare_tested_uds_features_with_reference(
                    f'/tmp/features_{match.match_name.replace("/", "_")}.json',
                    f'tests/test_json/test_integration/expected_features/test_isg_party_in_payload_format_customer_type_company/features_{match.match_name.replace("/", "_")}.json',
                )


class TestGrpcServerWithoutSSL(BaseGrpcTestCase.TestGrpcServer):

    stub = None

    @classmethod
    def setUpClass(cls):
        cls.tearDownClass()
        environment = os.environ.copy()
        service_config = ConsulServiceConfig()
        _ = subprocess.Popen("tests/scripts/start_services.sh", env=environment)
        channel = grpc.insecure_channel(
            f"{service_config.etl_service_ip}:{service_config.etl_service_port}"
        )
        TestGrpcServerWithoutSSL.stub = EtlPipelineServiceStub(channel)
        time.sleep(BaseGrpcTestCase.TestGrpcServer.TIMEOUT)
