from pipeline.config import (SPARK_APP_NAME, SPARK_DB_JARS, 
SPARK_DRIVER_MEMORY, SPARK_IVY2_DIR, 
SPARK_MASTER, SPARK_TMP_DIR, SPARK_USE_OPTIMAL_CONFIG, SPARK_USE_OPTIMAL_CONFIG)
import pyspark
import os
import psutil
import utils.config_service as configservice
import logging
# TODO: Integrate the spark configs to sparkservice
# import utils.spark_service as sparkservice
class Spark:
    exists = False
    spark_instance = None

    def spark_read_csv(self, file_path):
        return self.spark_instance.read.format('csv') \
                .option('header', True) \
                .option('multiline', True) \
                .option('escape', '"') \
                .load(file_path)

    def read_delta(self, file_path):
        return self.spark_instance.read.format('delta').load(file_path)
    
    def write_and_get_delta_data(self, df, delta_path: str, user_metadata: str = ''):
        """ Write the spark dataframe as delta lake format, read it and return the object.
            Both schema revolution and write mode set to overwrite.

        :param df: A spark dataframe.
        :param delta_path: The path of the delta lake to write to.
        :param user_metadata: User metadata for the delta transaction.
        :return: A spark dataframe read from the delta_path.
        """

        df = self.cast_array_null_to_array_string(df)
        df.write.format('delta') \
            .option('overwriteSchema', 'true') \
            .option('userMetadata', user_metadata) \
            .mode('overwrite') \
            .save(delta_path)

        return self.read_delta(delta_path)

    def show_dim(self, spark_df):
        """ Display the dimension of spark dataframe in numbers of rows and columns.

        :param spark_df: Passed in spark dataframe.
        :return: On screen display.
        """
        print('Dimension', spark_df.count(), len(spark_df.columns))


spark_instance = Spark()


def to_bytes(value):
    if value.endswith('g'):
        return int(value.replace('g','')) * 1024 * 1024 * 1024
    elif value.endswith('m'):
        return int(value.replace('m','')) * 1024 * 1024
    elif value.endswith('k'):
        return int(value.replace('k','')) * 1024
        
    return value

def validate_available_memory():
    properties = configservice.get_spark_config()
    
    available_bytes = psutil.virtual_memory().available
    
    driver_mem_bytes = to_bytes(properties['config']['spark.driver.memory'])
    executor_mem_bytes = to_bytes(properties['config']['spark.executor.memory'])
    
    required_bytes = driver_mem_bytes + executor_mem_bytes

    min_available_memory_1 = float(properties.get('min-available-memory-ratio', 0)) * required_bytes
    min_available_memory_2 = to_bytes(properties.get('min-available-memory-over-required', '0g')) + required_bytes
    
    min_available_memory = max(min_available_memory_1, min_available_memory_2)
    
    print(f'Available memory: {available_bytes}')
    print(f'Required memory: {min_available_memory}')
    
    if available_bytes < min_available_memory:
        raise Exception(
            f'Not enough available memory left: {available_bytes} bytes. Required at least: {min_available_memory} bytes')
    
    

validate_available_memory()

# Config and create Spark instance

import pyspark.sql.functions as F
from pyspark.sql.types import *
from pyspark.sql import SparkSession, SQLContext
from pyspark.sql.functions import udf

import helper.dbhelper as dbhelper

# Seems Spark 3 improved the join a lot, there is no need to use extra mergejoin jar
PYSPARK_SMALLEST_VERSION = '2.4.2'

if pyspark.__version__ >= '3.1':
    spark_jars_packages = 'io.delta:delta-core_2.12:1.0.0'
elif pyspark.__version__ >= '3.0':
    spark_jars_packages = 'io.delta:delta-core_2.12:0.8.0'
elif pyspark.__version__ >= PYSPARK_SMALLEST_VERSION:
    spark_jars_packages = 'io.delta:delta-core_2.11:0.6.1'
else:
    spark_jars_packages = ''
    raise ValueError(f'Delta lake is supported from pySpark {PYSPARK_SMALLEST_VERSION} onwards only.')
    
# spark_jars_packages = 'icom.hindog.spark:spark-mergejoin_2.11:2.0.1,org.postgresql:postgresql:42.2.20,com.oracle:ojdbc8:12.2.0.1'

# <!-- https://mvnrepository.com/artifact/io.delta/delta-core -->
# <dependency org="io.delta" name="delta-core_2.12" rev="1.0.0"/>

# <!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
# <dependency org="org.postgresql" name="postgresql" rev="42.2.20"/>

# <!-- https://mvnrepository.com/artifact/com.oracle.jdbc/ojdbc8 -->
# <dependency org="com.oracle.jdbc" name="ojdbc8" rev="12.2.0.1"/>

# <!-- https://mvnrepository.com/artifact/com.oracle.database.jdbc/ojdbc8 -->
# <dependency org="com.oracle.database.jdbc" name="ojdbc8" rev="21.1.0.0"/>

SPARK_MASTER, SPARK_DRIVER_MEMORY, SPARK_APP_NAME, SPARK_DB_JARS, SPARK_IVY2_DIR, spark_jars_packages, SPARK_TMP_DIR

# Spark object needs to be created first before importing "delta" module, e.g. from delta.tables import *
spark_conf = pyspark.SparkConf()

# spark configuration
spark_conf.setAll([('spark.master', SPARK_MASTER),
                   ('spark.driver.memory', SPARK_DRIVER_MEMORY),
                   ('spark.app.name', SPARK_APP_NAME),
                   ('spark.jars', SPARK_DB_JARS),
                   ('spark.jars.ivy', os.path.abspath(SPARK_IVY2_DIR)),
                   ('spark.jars.packages', spark_jars_packages),
#                    ('spark.submit.pyFiles', '%s,%s' % (EGG_PATH, SPARK_DELTA_JAR)),
#                    ('spark.submit.pyFiles', SPARK_DELTA_JAR),
                   ('spark.driver.extraJavaOpions', '-Djava.io.tmpdir=' + SPARK_TMP_DIR),
                   ('spark.local.dir', SPARK_TMP_DIR)])

spark_conf.setAll([("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension"),
                       ("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")])

import psutil, math
def set_spark_cpu_memory(spark_conf):
    def _get_optimal_ram():
        available_ram_gb = psutil.virtual_memory().available / (1024 ** 3)
        if available_ram_gb >= 64:
            ram_gb = math.ceil(available_ram_gb * 0.5)
        if available_ram_gb >= 32:
            ram_gb = math.ceil(available_ram_gb * 0.6)
        elif available_ram_gb >= 24:
            ram_gb = math.ceil(available_ram_gb * 0.7)
        elif available_ram_gb >= 16:
            ram_gb = math.ceil(available_ram_gb * 0.75)
        else:
            ram_gb = math.ceil(available_ram_gb * 0.8)
        
        return str(ram_gb) + 'g'

    cpu_count = psutil.cpu_count() / 2
    master = 'local[%d]' % cpu_count if SPARK_USE_OPTIMAL_CONFIG else SPARK_MASTER
    memory = _get_optimal_ram() if SPARK_USE_OPTIMAL_CONFIG else SPARK_DRIVER_MEMORY
    
    spark_conf.setAll([('spark.master', '%s' % master),
                       ('spark.driver.memory', '%s' % memory)
                      ])
    
    print('Actual Spark Master: %s' % master)
    print('Actual Spark Driver Memory: %s' % memory)


# spark_conf.setAll([('spark.sql.parquet.enableVectorizedReader', 'false')])
if Spark.spark_instance is None:
    set_spark_cpu_memory(spark_conf)
    spark = SparkSession.builder.config(conf=spark_conf).appName(SPARK_APP_NAME).getOrCreate()
    spark.sparkContext.addPyFile("/app/dependencies/ivy2/cache/io.delta/delta-core_2.12/jars/delta-core_2.12-1.0.0.jar")
    # delta package can only be called after spark object created




    # Config logging

    # log level needs to be set for sparkContext first to allow the subsequent logger taking effect
    spark.sparkContext.setLogLevel('warn')
    log4jlogger = spark._jvm.org.apache.log4j
    logger = log4jlogger.LogManager.getLogger(SPARK_APP_NAME)


    # alternatively, create the logger object, then set the log level is also fine
    # DO NOTE there is no need to set sparkContext log level for this case

    # log4jlogger = spark._jvm.org.apache.log4j
    # logger = log4jlogger.LogManager.getLogger(SPARK_APP_NAME)
    # logger.setLevel(log4jlogger.Level.INFO)

    logger.debug('Test debug log')
    logger.info('Test info log')
    logger.warn('Test warn log')

    logging.basicConfig(format='%(asctime)s - %(name)s %(levelname)s: %(message)s',
                        datefmt='%Y/%m/%d %H:%M:%S',
                        level=logging.INFO
                    )
    logging.debug('Test debug log, from logging')
    logging.info('Test info log, from logging')
    logging.warning('Test warn log, from logging')

    print(f'pySpark version: {pyspark.__version__}')

    print('Spark UI - %s' % spark.sparkContext.uiWebUrl)
    spark_instance.spark_instance = spark

