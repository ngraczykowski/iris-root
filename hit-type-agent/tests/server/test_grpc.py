import os
import pathlib
import subprocess
import time
import unittest

import grpc
import psutil
from agent_base.utils import Config

from temp_agent.hit_type_agent_pb2 import CheckTriggersRequest, StringList, TokensMap
from temp_agent.hit_type_agent_pb2_grpc import HitTypeAgentStub
from tests.agent.constant import TEST_CASES

PORT = Config().load_yaml_config("application.local.yaml")["agent"]["grpc"]["port"]
GRPC_ADDRESS = f"localhost:{PORT}"
TIMEOUT_SEC = 0.5


class ServerIsNotRunning(Exception):
    pass


def grpc_server_on(channel) -> bool:
    # https://stackoverflow.com/questions/45759491/how-to-know-if-a-grpc-server-is-available/61384353#61384353
    try:
        grpc.channel_ready_future(channel).result(timeout=TIMEOUT_SEC)
        return True
    except grpc.FutureTimeoutError:
        return False


def wait_for_server(address):
    channel = grpc.insecure_channel(address)
    counter = 0
    while counter < 10 and not grpc_server_on(channel):
        time.sleep(2)
        counter += 1
    if counter == 10:
        raise ServerIsNotRunning("Check server")


def kill_recursive(process_pid: int):
    process = psutil.Process(process_pid)
    for proc in process.children(recursive=True):
        proc.kill()
    process.kill()


def kill_process_on_the_port(port):
    kill = subprocess.Popen(
        "kill -9 $(netstat -ltnp | "
        f"grep -w :{port} | "
        "awk '{ print $7 }' | "
        "grep -o '[0-9]\\+' )".split()
    )
    kill.wait()


class TestServer(unittest.TestCase):
    def setUp(self):
        try:
            os.remove("config/application.yaml")
        except FileNotFoundError:
            pass
        self.config_path = pathlib.Path("./config/application.yaml")
        self.config_path.symlink_to("application.local.yaml")  # link to file from the same dir
        kill_process_on_the_port(PORT)
        self.server_process = subprocess.Popen("python -m hit_type.main -v --grpc".split())
        wait_for_server(GRPC_ADDRESS)

    def tearDown(self):
        kill_recursive(self.server_process.pid)
        self.config_path.unlink()
        time.sleep(TIMEOUT_SEC)

    def test_check_grpc_response(self):
        channel = grpc.insecure_channel(GRPC_ADDRESS)
        stub = HitTypeAgentStub(channel)
        for test_case in TEST_CASES:
            data = test_case["data"]
            for type_ in data["trigger_categories"]:
                data["trigger_categories"][type_] = StringList(
                    tokens=data["trigger_categories"][type_]
                )
            for analyzed_token in data["triggered_tokens"]:
                map_of_tokens = {}
                for found_token, list_of_fields in data["triggered_tokens"][analyzed_token].items():
                    tokens = StringList(tokens=list_of_fields)
                    map_of_tokens[found_token] = tokens

                data["triggered_tokens"][analyzed_token] = TokensMap(tokens_map=map_of_tokens)
            request = CheckTriggersRequest(**test_case["data"])
            resp = stub.CheckTriggers(request)
            result = test_case["result"]
            self.assertEqual(result["solution"], resp.solution)
            self.assertEqual(set(resp.reason.hit_categories), set(result["hit_categories"]))
            self.assertEqual(
                set(resp.reason.normal_categories),
                set(result["normal_categories"]),
            )
