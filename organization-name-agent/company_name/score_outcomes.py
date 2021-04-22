from enum import Enum


class Outcomes(Enum):
    NO_MATCH = 'No match'
    PARTIAL_MATCH = 'Partial match'
    MATCH = 'Match'
    DISABLED = 'Disabled'
    NO_DATA = 'No data'
