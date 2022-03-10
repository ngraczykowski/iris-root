from dataclasses import dataclass


@dataclass
class InputRecordField:
    name: str = ""
    isScreenable: str = ""
    value: str = ""
    sortOrder: str = ""
