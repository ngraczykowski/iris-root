import utils.config_service as configservice
import utils.file_service as fileservice
import utils.spark_service as sparkservice


spark = sparkservice.load_spark()


def read_to_df(path):
    if path.endswith("parquet") or path.endswith("parq"):
        return read_parquet(path)
    else:
        return read_csv_df(path)


def read_parquet(path):
    return spark.read.parquet(path)


def read_csv_df(path):
    csv_options = configservice.get_csv_options()
    return (
        spark.read.option("quote", csv_options["quote"])
        .option("escape", csv_options["escape"])
        .option("delimiter", csv_options["delimiter"])
        .csv(path, header=True, multiLine=True)
    )


def read_json(param):
    return spark.read.json(param)


def create_data_frame(data):
    return spark.createDataFrame(data)


def create_data_frame_with_schema(data, schema):
    return spark.createDataFrame(data=data, schema=schema)


def create_empty_data_frame(schema):
    return create_data_frame_with_schema(spark.sparkContext.emptyRDD(), schema)


def read_or_create_df(path: str, schema):
    if fileservice.file_exists(path):
        return read_to_df(path)
    else:
        return create_empty_data_frame(schema)
