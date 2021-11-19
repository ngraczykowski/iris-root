import psutil
import utils.config_service as configservice
import pyspark.sql.functions as F
from pyspark.sql import SparkSession, SQLContext
# spark_conf.setAll([('spark.sql.parquet.enableVectorizedReader', 'false')])

# delta package can only be called after spark object created
if pyspark.__version__ >= PYSPARK_SMALLEST_VERSION:
    from delta.tables import *
import helper.dbhelper as dbhelper

# TODO: Integrate the spark configs to sparkservice
# import utils.spark_service as sparkservice

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
set_spark_cpu_memory(spark_conf)
spark = SparkSession.builder.config(conf=spark_conf).appName(SPARK_APP_NAME).getOrCreate()
# log level needs to be set for sparkContext first to allow the subsequent logger taking effect
spark.sparkContext.setLogLevel('info')
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

try:
    print('Spark configs:')
    for i in spark_conf.getAll():
        print(i)
except:
    pass

print('Spark UI - %s' % spark.sparkContext.uiWebUrl)