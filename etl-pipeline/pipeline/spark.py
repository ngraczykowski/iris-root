from typing import Iterable
from pipeline.config import (SPARK_APP_NAME, SPARK_DB_JARS, 
SPARK_DRIVER_MEMORY, SPARK_IVY2_DIR, 
SPARK_MASTER, SPARK_TMP_DIR, SPARK_USE_OPTIMAL_CONFIG, SPARK_USE_OPTIMAL_CONFIG)
import pyspark
import os
import psutil
import utils.config_service as configservice
import logging
from silenteight.aia.alerts import AlertHitDictFactory, AlertHitExtractor
from pyspark.sql.types import StructType, ArrayType, StructField
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

    def cast_array_null_to_array_string(self, df):
        """ When a Spark dataframe has column type of ArrayType(NullType()), it cannot be saved as
        parquet or delta format. Hence to convert each and every such column to
        ArrayType(StringType())

        :param df: A spark dataframe, could have some columns with ArrayType(NullType())
        """
        for c in df.columns:
            if self.get_col_type(df, c) == ArrayType(NullType()):
                df = df.withColumn(c, F.col(c).cast(ArrayType(StringType())))

        return df

    def get_col_type(self, spark_df, col):
        """ Not a very sophisticated implementation, but does the job for most of the scenarios.
        DO NOTE StructType, nested MapType not supported.

        :param spark_df: The spark dataframe.
        :param col: The name of a column or the key of a MapType's item.
        :return: The spark data type of the column or None if not exists.
        """
        if self.is_col_exist(spark_df, col):
            # SQL is case insensitive for column name, hence it's a must to lower case the column names
            df_schema_dict = {f.name.lower(): f.dataType for f in spark_df.schema.fields}
            if '.' in col:
                # When the source column is a map item from MapType column
                col_name = col.split('.', 1)[0].lower()
                map_item_name = col.split('.', 1)[1].lower()
                return df_schema_dict[col_name].valueType
            else:
                return df_schema_dict[col.lower()]

        return None

    def show_dim(self, spark_df):
        """ Display the dimension of spark dataframe in numbers of rows and columns.

        :param spark_df: Passed in spark dataframe.
        :return: On screen display.
        """
        print('Dimension', spark_df.count(), len(spark_df.columns))
    def is_col_exist(self, spark_df, col):
        """ Checking if the column exist in the dataframe, MapType item is also supported

        :param spark_df: The spark dataframe.
        :param col: The name of a column or the key of a MapType's item.
        :return: A boolean indicating whether the column exists or not.
        """
        # SQL is case insensitive for column name, hence it's a must to lower case the column names
        # for key lookup later
        df_schema_dict = dict(spark_df.dtypes)
        df_schema_dict = {k.lower(): v for k, v in df_schema_dict.items()}

        if col.lower() in df_schema_dict.keys():
            return True
        else:
            if '.' in col:
                # When the source column is a map item from MapType column
                real_col = col.split('.', 1)[0]
                if real_col.lower() in df_schema_dict.keys():
                    return True
                else:
                    return False
            else:
                return False

    def group_count(self, spark_df,
                group_by_cols: Iterable,
                n: int = 20,
                window_cols: Iterable = None,
                return_pandas: bool = True):
        """ Group by the column(s), sort by descending count, calculate percentage
            and return as Pandas dataframe.

        :param spark_df: A spark dataframe.
        :param group_by_cols: Name of the column, or a list of columns to group by.
        :param n: Top n rows, use -1 or float('inf') to indicate return all rows.
        :param window_cols: Name of the column, or a list of columns to apply the window function.
        :param return_pandas: Specify to return a pandas dataframe or not.
        :return: A pandas or spark dataframe.
        """

        def _normalize_to_list(collection_or_str):
            if isinstance(collection_or_str, (list, tuple, list)):
                return list(collection_or_str)
            elif isinstance(collection_or_str, str):
                return [collection_or_str]
            else:
                raise ValueError('Variable type not valid', type(collection_or_str))

        def _backtick_elements(cols: list):
            return [f'`{i}`' for i in cols]

        group_by_cols = _normalize_to_list(group_by_cols)

        df = spark_df.groupBy(group_by_cols).count()
        df = df.selectExpr('*', 'round(count * 100 / sum(count) over (), 3) as percent')

        if window_cols is None:
            sql_order_by_str = 'count desc, ' + ', '.join(_backtick_elements(group_by_cols))
            df = df.selectExpr('*',
                            f'sum(count) over (order by {sql_order_by_str}) as count_cum_sum',
                            f'round(100 * sum(count) over (order by {sql_order_by_str}) \
                                    / sum(count) over(), 3 \
                                    ) as percent_cum_sum'
                            ) \
                .orderBy(F.col('count_cum_sum').asc())
        else:
            window_cols = _normalize_to_list(window_cols)
            order_by_cols = window_cols + [i for i in group_by_cols if i not in window_cols]

            sql_partition_by_str = ', '.join(_backtick_elements(window_cols))
            sql_order_by_str = 'window_sum desc, count desc, ' + ', '.join(
                _backtick_elements(order_by_cols))

            percent_window_col_name = f'`percent_window_{"_".join(window_cols)}`'
            percent_window_col_cum_sum_name = f'`percent_window_{"_".join(window_cols)}_cum_sum`'

            df = df.selectExpr('*',
                            f'sum(count) over (partition by {sql_partition_by_str}) as window_sum') \
                .selectExpr('*',
                            f'sum(count) over (order by {sql_order_by_str}) as count_cum_sum',
                            f'round(100 * sum(count) over (order by {sql_order_by_str}) \
                                    / sum(count) over (), 3 \
                                    ) as percent_cum_sum',
                            f'round(100 * count / window_sum, 3) as {percent_window_col_name}',
                            f'round(100 * sum(count) over ( \
                                                            partition by {sql_partition_by_str} \
                                                            order by {sql_order_by_str} \
                                                            ) \
                                    / window_sum, 3 \
                                    ) as {percent_window_col_cum_sum_name}'
                            ) \
                .orderBy(F.col('count_cum_sum').asc()) \
                .drop('window_sum')

        if n != -1 and n != float('inf'):
            df = df.limit(n)

        return df.toPandas() if return_pandas else df
    def reorder_cols(self, df, reference_col: str, *to_order_cols: tuple):
        """ Get the reordered columns and return the resulted dataframe.

        :param df: Passed in spark or pandas dataframe.
        :param reference_col: The reference/base column used to locate the starting index.
        :param to_order_cols: The list of columns to be moved right after the reference_col.
        """
        reordered_cols = self.get_ordered_cols(df, reference_col, *to_order_cols)
        if isinstance(df, pyspark.sql.dataframe.DataFrame):
            return df.select(reordered_cols)
        else:
            return df[reordered_cols]
    def get_ordered_cols(self, df, reference_col: str, *to_order_cols: tuple):
        """ Reorder the columns. The function finds the reference column and then move all the
            to_order_cols behind this reference column and return the reordered columns as a list.
            This function supports both Spark and Pandas dataframe.

        :param df: Passed in spark or pandas dataframe.
        :param reference_col: The reference/base column used to locate the starting index.
        :param to_order_cols: The list of columns to be moved right after the reference_col.
        """

        def _validate_to_order_cols(ordered_cols):
            non_exist_cols = []
            for col in [reference_col] + list(to_order_cols):
                if col not in ordered_cols:
                    non_exist_cols.append(col)

            if len(non_exist_cols) > 1:
                raise ValueError(f'Columns {", ".join(non_exist_cols)} don\'t exist in the dataframe')
            elif len(non_exist_cols) > 0:
                raise ValueError(f'Column {", ".join(non_exist_cols)} doesn\'t exist in the dataframe')

        def _pop_to_order_cols(ordered_cols):
            for col in to_order_cols:
                ordered_cols.pop(ordered_cols.index(col))

        def _insert_to_order_cols(ordered_cols):
            ref_col_idx = ordered_cols.index(reference_col)
            ordered_cols[ref_col_idx + 1:ref_col_idx + 1] = to_order_cols

        # Force cast the dataframe columns as a list to support both Spark and Pandas dataframe.
        # If not, the pandas dataframe returns an Index object which doesn't have pop() functions and
        # etcs.
        ordered_cols = list(df.columns)
        _validate_to_order_cols(ordered_cols)
        _pop_to_order_cols(ordered_cols)
        _insert_to_order_cols(ordered_cols)

        return ordered_cols

   
    def sql_merge_to_target_col_from_source_cols(self, df,
                                             target_col,
                                             *source_cols,
                                             return_array=False,
                                             keep_duplicates=False):
        """  It's assumed the source columns are either simple data types or ArrayType.
            Each and every source column is casted to an ArrayType first, and array_union() will
            be applied at last, duplicates removed.

        :param df: A spark dataframe.
        :param target_col: The target column name.
        :param source_cols: One or multiple columns with simple or array data types.
        :param return_array: If there is only one source column, specify to return as its original data
                            type or ArrayType. Default is False, means return original data type.
        :param keep_duplicates: Indicates whether to keep duplicates in the final result column. Default
                                is False, means perform de-duplication.
        :return: Spark SQL expression (Column type) to be executed by select() function, DO NOTE, it's not for
                selectExpr() function.
        """
        valid_source_cols = []
        for source_col in source_cols:
            if self.is_col_exist(df, source_col):
                valid_source_cols.append(source_col)

        if len(valid_source_cols) >= 2:
            # When there are multiple valid source columns, cast each of them to ArrayType and perform
            # array_union()
            sql_expr_list = []
            for valid_source_col in valid_source_cols:
                sql_expr_list.append(self.sql_cast_col_to_array(df, valid_source_col))

            # It turns out the array_union() is not the best option, because it takes only 2 arrays
            # as input. A better way implemented and duplicates removed.
            # return F.array_union(*sql_expr_list).alias(target_col)
            if keep_duplicates:
                return F.flatten(F.array(*sql_expr_list)).alias(target_col)
            else:
                return F.array_distinct(F.flatten(F.array(*sql_expr_list))).alias(target_col)
        elif len(valid_source_cols) == 1:
            # Depends on the flag of "return_array" to cast simple data type to ArrayType or not.
            if return_array:
                return self.sql_cast_col_to_array(df, valid_source_cols[0]).alias(target_col)
            else:
                return F.expr(f'{valid_source_cols[0]} as {target_col}')
        else:
            # When all source columns are invalid, return a null value or empty ArrayType depends on the
            # flag
            if return_array:
                return F.array().alias(target_col)
            else:
                return F.lit(None).alias(target_col)
    def sql_cast_col_to_array(self, df, col):
        """ When it's ArrayType, need to make sure the None value is casted to [], if not,
            the array_union() will return None. The operation is equivalent to below one:
                F.coalesce('country_agent_ap_aliases', F.array())

            When it's simple data type, the operation is equivalent to below one:
                F.when(F.isnull('country_agent_ap'), F.array()) \
                .otherwise(F.array('country_agent_ap'))
            Also note the original column data type maintained as inner element's data type from the
            returned array.

        """
        dtype = self.get_col_type(df, col)
        if isinstance(dtype, ArrayType):
            return F.coalesce(col, F.array())
        else:
            return F.when((F.isnull(col)) | (F.lower(col) == 'none') | (F.col(col) == ''),
                        F.array()
                        ) \
                .otherwise(F.array(col))



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

