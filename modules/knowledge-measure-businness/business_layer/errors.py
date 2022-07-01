class KMBError(Exception):
    def __init__(self, message: str):
        self.message = message

    def __str__(self):
        return self.message


class ConfigurationError(KMBError):
    def __init__(self, message: str = "Invalid configuration file!"):
        super().__init__(message=message)


class PolicyStepsError(KMBError):
    def __init__(self, message: str):
        super().__init__(message=message)


class SolveInputDataError(KMBError):
    def __init__(self, message: str = "Wrong name in input 'data' keys!"):
        super().__init__(message=message)
