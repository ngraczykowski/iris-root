from business_layer.api import BaseCustomMeasure, FieldMeasure, ValueMeasure


class Sanctions(BaseCustomMeasure):
    """An example of a custom measure feature"""

    def __init__(self, domain_config_parameters):
        self.config = domain_config_parameters

        self.sanctioned_countries = [
            "North Korea",
            "Iran",
            "Syria",
            "Democratic People's Republic of Korea",
            "Cuba",
            "Crimea Region",
        ]
        self.context = self.config.context
        self.field_name = self.config.field

    def reduce_value_measure_results(self, value_measures):
        if not value_measures:
            return "NO_DATA"
        for value_measure in value_measures:
            if value_measure.evaluation == "TRUE":
                return value_measure.evaluation
        return "FALSE"

    def run(self, data, knowledge, measures):
        value_measures = []
        for field_value in data[f"ap_all_{self.field_name}_aggregated"]:
            for sanc_ctry in self.sanctioned_countries:
                if sanc_ctry in field_value:
                    value_measure = ValueMeasure(
                        ap_value=field_value, wl_value=sanc_ctry, evaluation="TRUE"
                    )
                else:
                    value_measure = ValueMeasure(
                        ap_value=field_value, wl_value=sanc_ctry, evaluation="FALSE"
                    )
                value_measures.append(value_measure)
        recommendation = self.reduce_value_measure_results(value_measures)
        field_measure = FieldMeasure(
            recommendation=recommendation, results=value_measures, context=self.context
        )
        return field_measure
