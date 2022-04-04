import json
import os
import threading
from typing import Callable

from locust import HttpUser, task, between


# from locust import events
# from locust_plugins.listeners import Print


class RequestFilesNotFound(Exception):
    pass


def request_file_generator_supplier(request_file_dir: str) -> Callable:
    def _continous_request_file_generator() -> map:
        while True:
            counter = 0
            for rf_json in _request_file_generator():
                counter += 1
                yield rf_json

            if counter == 0:
                raise RequestFilesNotFound()

    def _request_file_generator() -> map:
        for dirpath, _, filenames in os.walk(request_file_dir):
            for fn in filenames:
                fp = os.path.join(dirpath, fn)
                if os.path.isfile(fp) and fp.lower().endswith('.json'):
                    with open(fp) as f:
                        try:
                            json_to_post = json.load(f)
                            yield json_to_post
                        except Exception:
                            continue

    return _continous_request_file_generator


def thread_safe_request_file_generator_supplier(generator_func: Callable) -> Callable:
    _lock = threading.Lock()
    _iter = generator_func()

    def _thread_safe_generator():
        with _lock:
            return next(_iter)

    return _thread_safe_generator


req_file_gen = thread_safe_request_file_generator_supplier(
    request_file_generator_supplier(os.environ['TBT_REQUEST_FILES_DIR']))


# @events.init.add_listener
# def on_locust_init(environment, **_kwargs):
#     Print(environment, include_time=True, include_length=True)


class SEARFlood(HttpUser):
    wait_time = between(1, 3)
    dump_response = bool(int(os.environ.get('TBT_DUMP_RESPONSE') or '0'))

    @task
    def alert(self):
        headers={'Content-Type': 'application/json'}
        if os.environ.get('TOKEN') is not None:
          headers['Authorization'] = 'Bearer ' + os.environ['TOKEN']
        r = self.client.post(
            '/alert?dc=UK',
            timeout=30,
            data=json.dumps(req_file_gen()),
            headers=headers
        )
        if SEARFlood.dump_response:
            print(r.json())
