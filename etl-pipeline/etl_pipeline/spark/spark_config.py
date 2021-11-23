import os

import pyspark

from etl_pipeline.config.config import (
    SPARK_APP_NAME,
    SPARK_DB_JARS,
    SPARK_DRIVER_MEMORY,
    SPARK_IVY2_DIR,
    SPARK_MASTER,
    SPARK_TMP_DIR,
)

PYSPARK_SMALLEST_VERSION = "2.4.2"


if pyspark.__version__ >= "3.1":
    spark_jars_packages = "io.delta:delta-core_2.12:1.0.0"
elif pyspark.__version__ >= "3.0":
    spark_jars_packages = "io.delta:delta-core_2.12:0.8.0"
elif pyspark.__version__ >= PYSPARK_SMALLEST_VERSION:
    spark_jars_packages = "io.delta:delta-core_2.11:0.6.1"
else:
    spark_jars_packages = ""
    raise ValueError(
        f"Delta lake is supported from pySpark {PYSPARK_SMALLEST_VERSION} onwards only."
    )

spark_conf = pyspark.SparkConf()

spark_conf.setAll(
    [
        ("spark.master", SPARK_MASTER),
        ("spark.driver.memory", SPARK_DRIVER_MEMORY),
        ("spark.app.name", SPARK_APP_NAME),
        ("spark.jars", SPARK_DB_JARS),
        ("spark.jars.ivy", os.path.abspath(SPARK_IVY2_DIR)),
        ("spark.jars.packages", spark_jars_packages),
        ("spark.databricks.delta.retentionDurationCheck.enabled", "false"),
        ("spark.driver.extraJavaOpions", "-Djava.io.tmpdir=" + SPARK_TMP_DIR),
        ("spark.local.dir", SPARK_TMP_DIR),
    ]
)

spark_conf.setAll(
    [
        ("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension"),
        ("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog"),
    ]
)
