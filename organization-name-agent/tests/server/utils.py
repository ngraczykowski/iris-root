import subprocess

import grpc
import psutil


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


class ServerIsNotRunning(Exception):
    pass


def grpc_server_on(channel, timeout) -> bool:
    # https://stackoverflow.com/questions/45759491/how-to-know-if-a-grpc-server-is-available/61384353#61384353
    try:
        grpc.channel_ready_future(channel).result(timeout=timeout)
        return True
    except grpc.FutureTimeoutError:
        return False


TEST_CASES = [
    {
        "data": {"alerted_party_names": ["ABC"], "watchlist_party_names": ["ABCDE"]},
        "solution": "MATCH",
        "probability": 1,
        "results_number": 1,
    },
    {
        "data": {"alerted_party_names": ["ABC"], "watchlist_party_names": ["DEF"]},
        "solution": "NO_MATCH",
        "probability": 0.98,
        "results_number": 1,
    },
    {
        "data": {
            "alerted_party_names": ["HP", "JP Morgan", "SCB"],
            "watchlist_party_names": ["Silent Eight", "Hewlett & Packard"],
        },
        "solution": "MATCH",
        "probability": 0.8,
        "results_number": 6,
    },
    {
        "data": {"alerted_party_names": [], "watchlist_party_names": []},
        "solution": "NO_DATA",
        # no probability here as it is 'NO_DATA'
        "results_number": 0,
    },
]
