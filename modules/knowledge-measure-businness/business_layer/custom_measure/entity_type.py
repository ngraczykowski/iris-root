from business_layer.api import BaseCustomMeasure, FieldMeasure


class EntityType(BaseCustomMeasure):
    """An example of a custom measure feature"""

    def __init__(self, domain_config_parameters):
        self.config = domain_config_parameters
        self.context = self.config.context
        self.field_name = self.config.field

    def reduce_value_measure_results(self, value_measures):
        pass

    def run(self, data, knowledge, measures):
        recommendation = data[self.field_name]
        field_measure = FieldMeasure(
            recommendation=recommendation, results=[], context=self.context
        )
        return field_measure
