import collections
import dataclasses
import json


class CompanyNamesEncoder(json.JSONEncoder):
    def default(self, obj):
        if dataclasses.is_dataclass(obj):
            return {k: self.default(getattr(obj, k)) for k in dataclasses.asdict(obj)}
        if isinstance(obj, collections.UserList):
            return obj.data
        return obj
