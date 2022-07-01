import json
import os


def read_json_resource(filename: str):
    with open(os.path.join("tests/service_based/wrappers/resources", filename)) as file:
        data = json.load(file)
    return data
