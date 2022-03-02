import os
import re

from custom.aia.config import (
    ALERT_NOTES_FILE_NAME,
    ALERTS_FILE_NAME,
    APPLICATION_DATA_DIR,
    CLEANSED_DATA_DIR,
    RAW_DATA_DIR,
    STANDARDIZED_DATA_DIR,
)
from custom.aia.transformations import custom_transform
from custom.aia.xml_pipeline import AIAXMLPipeline
from etl_pipeline.agent_input_creator.input_creator import create_input_for_agents
from etl_pipeline.pipeline import ETLPipeline


class AIAPipeline(ETLPipeline):
    def convert_raw_to_standardized(
        self, raw_data_path: str = RAW_DATA_DIR, target_path: str = STANDARDIZED_DATA_DIR
    ):
        """Converts raw data to delta format.

        Parameters
        ----------
        raw_data_path : str, optional
            by default RAW_DATA_DIR
        target_path : str, optional
            by default STANDARDIZED_DATA_DIR
        """
        for file_name in os.listdir(raw_data_path):
            raw_file_path = os.path.join(raw_data_path, file_name)
            standardized_file_name = re.sub(".csv$", ".delta", file_name)
            standardized_file_path = os.path.join(target_path, standardized_file_name)
            df = self.engine.load_raw_data(raw_file_path)
            self.engine.save_data(df, standardized_file_path, return_df=False)

    def transform_standardized_to_cleansed(self):
        xml_pipeline = AIAXMLPipeline(engine=self.engine)
        alert_ap_wl_hit_names_df = xml_pipeline.pipeline(
            os.path.join("data/2.standardized/", ALERTS_FILE_NAME)
        )
        alert_ap_wl_hit_names_df = self.merge_and_clean_empty_hits(alert_ap_wl_hit_names_df)
        custom_transform(alert_ap_wl_hit_names_df, self.engine.spark_instance)

    def transform_cleansed_to_application(self):
        cleansed_alert_df = self.engine.load_data(
            os.path.join(CLEANSED_DATA_DIR, ALERTS_FILE_NAME)
        )
        cleansed_note_df = self.engine.load_data(
            os.path.join(CLEANSED_DATA_DIR, ALERT_NOTES_FILE_NAME)
        )
        cleansed_note_df = cleansed_note_df.where('analyst_note_stage like "%last%"').selectExpr(
            "ALERT_ID", "note as last_note"
        )
        cleansed_alert_df = cleansed_alert_df.join(cleansed_note_df, "ALERT_ID", how="left")
        create_input_for_agents(
            cleansed_alert_df, APPLICATION_DATA_DIR, spark_instance=self.engine.spark_instance
        )
