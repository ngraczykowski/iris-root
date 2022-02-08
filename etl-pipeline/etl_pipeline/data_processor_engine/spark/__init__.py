from etl_pipeline.data_processor_engine.spark.spark import SparkProcessingEngine  # noqa: F401

spark_engine = SparkProcessingEngine()
spark_instance = spark_engine.spark_instance
