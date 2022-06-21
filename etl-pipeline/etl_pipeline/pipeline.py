import functools
import os
from abc import ABC

from omegaconf import OmegaConf

from etl_pipeline.config import CONFIG_APP_DIR, pipeline_config
from etl_pipeline.data_processor_engine.engine.engine import ProcessingEngine
from etl_pipeline.data_processor_engine.json_engine.json_engine import JsonProcessingEngine
from pipelines.ms.functions import Functions


class ETLPipeline(ABC):
    def __init__(self, engine: ProcessingEngine, config=None):
        self.engine: ProcessingEngine = engine
        self.engine.pipeline_config = config
        self.pipeline_config = config

    def convert_raw_to_standardized(self):
        pass

    def transform_standardized_to_cleansed(self):
        pass

    def transform_cleansed_to_application(self):
        pass

    @classmethod
    def build_pipeline(self, pipeline_type):
        flow_config = OmegaConf.load(os.path.join(CONFIG_APP_DIR, "pipeline", "flow.yaml"))
        func_map = {}
        for column_transformation in flow_config:
            pipeline_func: dict = flow_config[column_transformation]
            target_collection = pipeline_func.pop("target_collection", None)
            method = pipeline_func.pop("method", None)
            if method is None:
                method_name = pipeline_func.pop("method_name", None)
                pattern_method = pipeline_func.pop("pattern_method")
            else:
                method_name = method
                pattern_method = f"_{method}"
            pipeline_func.update({"target_field": column_transformation})
            if method_name is None:
                method_name = f"{column_transformation}_{pattern_method}"
            if target_collection is not None:
                pipeline_func.update({"target_collection": target_collection})
            try:
                func = getattr(Functions, pattern_method)
            except AttributeError:
                raise AttributeError(
                    f"Method is unknown {pattern_method}. "
                    " You need to define a method to the Functions"
                )

            func_map[method_name] = functools.partial(func, **pipeline_func)
        functions = Functions(func_map)
        engine = JsonProcessingEngine(pipeline_config)
        return pipeline_type(engine, pipeline_config, functions)
