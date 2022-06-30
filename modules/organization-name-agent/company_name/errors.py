class ComparisonError(Exception):
    def __init__(self, first: str, second: str):
        self.message = "Error when comparing names '{0}' and '{1}'".format(first, second)

    def __str__(self):
        return self.message
