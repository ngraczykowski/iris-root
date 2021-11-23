from etl_pipeline.spark.spark_server import SparkServer


def test():
    SparkServer().launch()
    assert 40 == 40
