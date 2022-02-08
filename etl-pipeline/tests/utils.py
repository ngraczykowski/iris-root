import json
import pickle


def compare_dataframe(dataframe_1, dataframe_2, unique_column="ALERT_INTERNAL_ID"):
    dataframe_1_rows = dataframe_1.sort(unique_column, ascending=False).collect()
    dataframe_2_rows = dataframe_2.sort(unique_column, ascending=False).collect()
    if len(dataframe_1_rows) == len(dataframe_2_rows):
        for tested_row, reference_row in zip(dataframe_1_rows, dataframe_2_rows):
            if tested_row != reference_row:
                raise AssertionError(f"Tested row: {tested_row}\nShould be: {reference_row}")
    return True


def load_pickle(filename):
    with open(filename, "rb") as f:
        return pickle.load(f)


def load_xml(filename):
    with open(filename, "r") as f:
        return f.read()


def load_json(filename):
    with open(filename, "r") as f:
        return json.load(f)
