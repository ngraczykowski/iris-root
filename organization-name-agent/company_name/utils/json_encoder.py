import collections
import dataclasses
import enum
import json


class CompanyNameEncoder(json.JSONEncoder):
    def default(self, obj):
        if dataclasses.is_dataclass(obj):
            return {k: getattr(obj, k) for k in dataclasses.asdict(obj)}
        if isinstance(obj, collections.UserList):
            return obj.data
        if isinstance(obj, enum.Enum):
            return obj.value
        return repr(obj)
