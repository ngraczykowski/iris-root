from etl_pipeline.spark.spark_server import SparkServer

SparkServer().launch()
import time

while True:
    time.sleep(3500)
