import subprocess

import grpc
import psutil


def grpc_server_on(channel, timeout) -> bool:
    # https://stackoverflow.com/questions/45759491/how-to-know-if-a-grpc-server-is-available/61384353#61384353
    try:
        grpc.channel_ready_future(channel).result(timeout=timeout)
        return True
    except grpc.FutureTimeoutError:
        return False


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
