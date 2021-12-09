import os
import logging
import time
from pipeline.config import RAW_DAT_DIR, in_raw_data_dir
import re


# IMPLEMENTATION: Spark manager
def spark_read_csv(file_path):
    return spark.read.format('csv') \
                .option('header', True) \
                .option('multiline', True) \
                .option('escape', '"') \
                .load(file_path)

# IMPLEMENTATION: DeltaConverter
for file_name in os.listdir(RAW_DATA_DIR):
    start = time.time()
    raw_file_path = in_raw_data_dir(file_name)
    logging.info(f'Start to process {raw_file_path}')
    
    standardized_file_name = re.sub('.csv$', '.delta', file_name)
    standardized_file_path = in_standardized_data_dir(standardized_file_name)
    
    df = spark_read_csv(raw_file_path)
    df = write_read_delta(spark, df, standardized_file_path)
    
    logging.info(f'Data saved to {standardized_file_path}')
    logging.info(f'Time lapsed {time.time() - start:.2f} s')
    
    print()
    time.sleep(1)
