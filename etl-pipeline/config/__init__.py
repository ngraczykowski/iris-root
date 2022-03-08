import os

from omegaconf import OmegaConf

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]

conf = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "config_app.yaml"))
pipeline_config = conf.PIPELINE
columns_namespace = conf.SPARK_DATAFRAME_COLUMNS
