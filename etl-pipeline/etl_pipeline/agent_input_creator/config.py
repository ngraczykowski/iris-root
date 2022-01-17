import json
import os
from typing import Dict, List


def load_agent_input_config(file_name: str):
    file_path = os.path.join("etl_pipeline/agent_input_creator/", file_name)
    with open(file_path, "r") as file:
        input_config = json.load(file)
    return input_config


AGENT_INPUT_CONFIG: Dict[str, Dict[str, List[str]]] = load_agent_input_config(
    "agent_input_config.json"
)
AGENT_INPUT_AGG_COL_CONFIG: Dict[str, Dict[str, List[str]]] = load_agent_input_config(
    "agent_input_agg_col_config.json"
)
