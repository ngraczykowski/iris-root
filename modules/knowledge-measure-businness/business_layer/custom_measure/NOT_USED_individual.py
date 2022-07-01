from business_layer.api import BaseCustomMeasure, FieldMeasure, ValueMeasure


class Individual(BaseCustomMeasure):
    """An example of a custom measure feature"""

    def __init__(self, domain_config_parameters):
        self.config = domain_config_parameters

        self.threshold = 0.9

        self.field_name = self.config.field

    def reduce_value_measure_results(self, value_measures):
        if not value_measures:
            return "NO_DATA"
        for value_measure in value_measures:
            if value_measure.evaluation == "MATCH":
                return value_measure.evaluation
        return "NO_MATCH"

    def run(self, data, knowledge, measures):
        wl_individual_vs_org_indicator = data["wl_type"]
        if wl_individual_vs_org_indicator != "INDIVIDUAL":
            return FieldMeasure(recommendation="NOT_APPLICABLE", results=[], context="name")

        org_name_measure = measures["org_name_name"]
        value_measures = []
        for result in org_name_measure.results:
            fuzzy_on_base_score = next(
                score for score in result.metrics if score.name == "fuzzy_on_base"
            )
            if float(fuzzy_on_base_score.value) >= float(self.threshold):
                evaluation = "MATCH"
            else:
                evaluation = "NO_MATCH"
            value_measure = ValueMeasure(
                ap_value=", ".join(fuzzy_on_base_score.compared.alerted_party_items),
                wl_value=",".join(fuzzy_on_base_score.compared.watchlist_party_items),
                evaluation=evaluation,
            )
            value_measures.append(value_measure)

        recommendation = self.reduce_value_measure_results(value_measures)
        field_measure = FieldMeasure(
            recommendation=recommendation, results=value_measures, context="name"
        )
        return field_measure
