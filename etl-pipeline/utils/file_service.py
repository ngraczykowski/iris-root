import builtins
import glob
import os
import shutil
from datetime import datetime
from pathlib import Path

import utils.config_service as configservice


def __ensure_dir_exists(path):
    dir = os.path.dirname(path)
    Path(dir).mkdir(parents=True, exist_ok=True)


def __save_to_csv(df, filepath):
    filename = os.path.basename(filepath)
    tmp_file_dir = configservice.get_tmp_dir()
    tmp_file_name = filename + "_" + datetime.now().isoformat()
    tmp_file_path = str(Path(tmp_file_dir).joinpath(tmp_file_name))

    __ensure_dir_exists(tmp_file_path)

    csv_options = configservice.get_csv_options()
    df.repartition(1).write.csv(
        tmp_file_path,
        header=True,
        quote=csv_options["quote"],
        escape=csv_options["escape"],
        emptyValue="",
    )

    __ensure_dir_exists(filepath)
    os.system("cat " + tmp_file_path + "/p* > " + filepath)
    shutil.rmtree(tmp_file_path)

    print("save file: " + filepath)


def __save_to_parquet(df, filepath):
    __ensure_dir_exists(filepath)
    df.write.mode("overwrite").parquet(filepath)


def save_df(df, filepath):
    if filepath.endswith("parquet") or filepath.endswith("parq"):
        __save_to_parquet(df, filepath)
    elif filepath.endswith("csv"):
        __save_to_csv(df, filepath)
    else:
        raise Exception(
            "Not supported file type in file {}, set parquet or csv file extension.",
            filepath,
        )


def file_exists(filepath: str):
    return os.path.exists(filepath)


def copy_file(source: str, destination: str):
    __ensure_dir_exists(destination)
    shutil.copy(src=source, dst=destination)
    print(f"copy file: {source} -> {destination}")


def get_latest_file(*path_pattern_partials):
    list_of_files = glob.glob(os.path.join(*path_pattern_partials))
    print("possible files:", list_of_files)
    return builtins.max(list_of_files, key=os.path.getctime)
