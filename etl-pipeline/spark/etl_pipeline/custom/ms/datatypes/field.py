from dataclasses import dataclass


@dataclass
class InputRecordField:
    name: str = ""
    isScreenable: str = ""
    value: str = ""
    sortOrder: str = ""

    def __hash__(self):
        hashed = hash((getattr(self, key) for key in self.__annotations__))
        return hashed
