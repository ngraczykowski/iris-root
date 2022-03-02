from omegaconf import OmegaConf

conf = OmegaConf.load("config/config_app.yaml")
pipeline_config = conf.PIPELINE
columns_namespace = conf.SPARK_DATAFRAME_COLUMNS
