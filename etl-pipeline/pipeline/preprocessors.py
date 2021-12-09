import os
import logging
import time


from pipeline.config import RAW_DATA_DIR, STANDARDIZED_DATA_DIR, in_raw_data_dir, in_standardized_data_dir
import re
from pipeline.spark import spark_instance
from delta.tables import *

from silenteight.data.spark import preprocess

import helper.dbhelper as dbhelper

# IMPLEMENTATION: DeltaConverter
def convert_to_standardized(raw_data_path=RAW_DATA_DIR, target_path=STANDARDIZED_DATA_DIR):
    for file_name in os.listdir(raw_data_path):
        start = time.time()
        raw_file_path = in_raw_data_dir(file_name)
        logging.info(f'Start to process {raw_file_path}')
        
        standardized_file_name = re.sub('.csv$', '.delta', file_name)
        standardized_file_path = in_standardized_data_dir(standardized_file_name)
        print(spark_instance.spark_instance)
        print(raw_data_path)
        df = spark_instance.spark_read_csv(raw_file_path)
        print(raw_data_path)
        
        df = preprocess.write_read_delta(spark_instance.spark_instance, df, standardized_file_path)
        
        logging.info(f'Data saved to {standardized_file_path}')
        logging.info(f'Time lapsed {time.time() - start:.2f} s')
        
        print()
        time.sleep(1)
