import os

from omegaconf import OmegaConf

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
pipeline_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "pipeline", "pipeline.yaml"))
columns_namespace = pipeline_config.SPARK_DATAFRAME_COLUMNS
pipeline_config = pipeline_config.PIPELINE
service_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))
agents_config = {
    "WM_ADDRESS": OmegaConf.load(
        os.path.join(CONFIG_APP_DIR, "agents", "agents_input_WM_ADDRESS.yaml")
    )
}
