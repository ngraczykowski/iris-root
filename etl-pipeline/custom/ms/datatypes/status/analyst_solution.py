from enum import Enum


class AnalystSolution(Enum):
    TRUE_POSITIVE = "TP"
    RISK_ACCEPTED = "RA"
    FALSE_POSITIVE = "FP"
    OTHER = "O"
