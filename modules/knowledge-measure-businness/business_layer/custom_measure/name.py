from business_layer.api import BaseCustomMeasure, FieldMeasure


class Name(BaseCustomMeasure):
    """An example of a custom measure feature"""

    def __init__(self, domain_config_parameters):
        self.config = domain_config_parameters
        self.context = self.config.context

    def reduce_value_measure_results(self, value_measures):
        pass

    def run(self, data, knowledge, measures):

        entity_type = data["wl_type"]
        if entity_type == "INDIVIDUAL":
            field_measure = measures[f"individual_{self.context}"]
            name_solution = field_measure.recommendation
            results = field_measure.results

        elif entity_type == "ORGANIZATION":
            field_measure = measures[f"org_{self.context}"]
            name_solution = field_measure.recommendation
            results = field_measure.results

        else:
            name_solution = "NOT_APPLICABLE"
            results = []

        return FieldMeasure(recommendation=name_solution, results=results, context=self.context)
