import collections
import dataclasses
import enum
import json


def simplify(obj):
    if dataclasses.is_dataclass(obj):
        return {k: simplify(getattr(obj, k)) for k in dataclasses.asdict(obj)}
    if isinstance(obj, collections.UserList):
        return simplify(obj.data)
    if isinstance(obj, enum.Enum):
        return obj.value
    if isinstance(obj, (tuple, list)):
        return [simplify(o) for o in obj]
    if isinstance(obj, dict):
        return {k: simplify(v) for k, v in obj.items()}
    return obj


class HitTypeEncoder(json.JSONEncoder):
    def default(self, obj):
        return simplify(obj)
