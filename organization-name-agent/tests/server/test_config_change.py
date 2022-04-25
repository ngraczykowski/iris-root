import subprocess
import time

import grpc
import pytest
from agent_base.utils import Config
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2 import (
    CompareOrganizationNamesRequest,
)
from silenteight.agent.organizationname.v1.api.organization_name_agent_pb2_grpc import (
    OrganizationNameAgentStub,
)

from company_name.agent.config_service.organization_name_agent_config_pb2 import (
    ChangeOrganizationNameAgentConfigRequest,
    FeatureRule,
    ModelSolutionRule,
    NameAlphabetRule,
    NameInclusionRule,
    NameLengthRule,
)
from company_name.agent.config_service.organization_name_agent_config_pb2_grpc import (
    OrganizationNameAgentConfigStub,
)
from tests.server.test_grpc import wait_for_server
from tests.server.utils import kill_process_on_the_port, kill_recursive

ESTIMATED_TIMEOUT = 0.5
PORT = Config().load_yaml_config("application.yaml")["agent"]["grpc"]["port"]
GRPC_ADDRESS = f"localhost:{PORT}"


@pytest.fixture
def run_service():
    kill_process_on_the_port(PORT)
    server_process = subprocess.Popen("python -m company_name.main -v --grpc".split())
    wait_for_server(GRPC_ADDRESS)
    yield
    kill_recursive(server_process.pid)
    time.sleep(ESTIMATED_TIMEOUT)


@pytest.mark.parametrize(
    "tested_case, before_change_solution, change_request, after_change_solution",
    [
        (
            {"alerted_party_names": ["BA$$"], "watchlist_party_names": ["Some Name"]},
            "NO_MATCH",
            ChangeOrganizationNameAgentConfigRequest(
                name_alphabet_rule=NameAlphabetRule(
                    min_acceptable_fraction=1, acceptable_alphabets=["LATIN"]
                )
            ),
            "INCONCLUSIVE",
        ),
        (
            {"alerted_party_names": ["Silent Eight"], "watchlist_party_names": ["Microsoft"]},
            "NO_MATCH",
            ChangeOrganizationNameAgentConfigRequest(
                name_inclusion_rule=NameInclusionRule(without_part=[], without_token=["Silent"])
            ),
            "INCONCLUSIVE",
        ),
        (
            {"alerted_party_names": ["Silent Eight"], "watchlist_party_names": ["Microsoft"]},
            "NO_MATCH",
            ChangeOrganizationNameAgentConfigRequest(
                name_inclusion_rule=NameInclusionRule(
                    without_part=["soft"], without_token=["Apple"]
                )
            ),
            "INCONCLUSIVE",
        ),
        (
            {"alerted_party_names": ["Silent Eight"], "watchlist_party_names": ["Microsoft"]},
            "NO_MATCH",
            ChangeOrganizationNameAgentConfigRequest(
                name_length_rule=NameLengthRule(max_length=5, max_unique_tokens=5)
            ),
            "INCONCLUSIVE",
        ),
        (
            {"alerted_party_names": ["Silent Eight"], "watchlist_party_names": ["SE"]},
            "MATCH",
            ChangeOrganizationNameAgentConfigRequest(
                feature_rules=[
                    FeatureRule(
                        feature="abbreviation",
                        threshold=0.9,
                        solution="NO_MATCH",
                        solution_probability=0.9,
                    )
                ]
            ),
            "NO_MATCH",
        ),
        (
            {"alerted_party_names": ["Silent Eight"], "watchlist_party_names": ["Hello Silencio"]},
            "NO_MATCH",
            ChangeOrganizationNameAgentConfigRequest(
                model_solution_rules=[
                    ModelSolutionRule(
                        threshold=0.95,
                        solution="NO_MATCH",
                        label="NO_MATCH",
                    ),
                    ModelSolutionRule(
                        threshold=0,
                        solution="MATCH",
                        label="MATCH",
                    ),
                ]
            ),
            "MATCH",
        ),
    ],
)
def test_passing_new_config(
    tested_case, before_change_solution, change_request, after_change_solution, run_service
):
    channel = grpc.insecure_channel(GRPC_ADDRESS)
    config_stub = OrganizationNameAgentConfigStub(channel)
    call_stub = OrganizationNameAgentStub(channel)

    request = CompareOrganizationNamesRequest(**tested_case)
    before_change_result = call_stub.CompareOrganizationNames(request)
    assert before_change_result.solution == before_change_solution

    change_status = config_stub.ChangeOrganizationNameAgentConfig(change_request)
    assert change_status.status == 1  # means 'OK'

    after_change_result = call_stub.CompareOrganizationNames(request)
    assert after_change_result.solution == after_change_solution
