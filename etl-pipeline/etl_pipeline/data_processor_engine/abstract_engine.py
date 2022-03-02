from abc import ABC


class Engine(ABC):
    def load_raw_data(self):
        pass

    def save_data(self):
        pass

    def merge_to_target_col_from_source_cols(self):
        pass

    def substitute_nulls_in_array_with_new_values(self):
        pass

    def load_data(self):
        pass
