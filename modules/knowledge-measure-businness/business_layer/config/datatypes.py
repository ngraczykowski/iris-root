import enum
from dataclasses import dataclass
from typing import Any, Callable, ClassVar, Dict, List, Union

from business_layer.comment.sentiment_config import SentimentCommentConfig
from business_layer.errors import ConfigurationError


class ConfigField(enum.Enum):
    COMMENT_INTRO = "comment_intro"
    DECISIONS = "decisions"
    DECISIONS_DETAILS_COMMENT_TYPE = "comment_type"
    DECISIONS_DETAILS_VERBAL = "verbal"
    FEATURE_MAPPING = "feature_mapping"
    DOMAIN_CONFIG_FIELDS = "fields"
    DOMAIN_CONFIG_CONTEXTS = "contexts"
    DOMAIN_CONFIG_COMMENTS = "comments"
    DOMAIN_CONFIG_IGNORE = "ignore"


@dataclass
class Comment:
    verbal: str
    type: str


class DomainSource(enum.Enum):
    CUSTOM_FEATURES = "custom_features"
    STANDARD_FEATURES = "standard_features"


class DomainType(enum.Enum):
    KNOWLEDGE = "knowledge"
    MEASURE = "measure"


@dataclass
class Domain:
    name: str
    source: DomainSource
    type_name: DomainType
    fields: List[str]
    contexts: List[str]
    comments: Dict[str, Any]
    ignore: bool

    def __post_init__(self):
        try:
            self.source = DomainSource(self.source)
            self.type_name = DomainType(self.type_name)
        except ValueError:
            raise ConfigurationError(f"Invalid config for domain {self.name}!")

        if self.type_name == DomainType.MEASURE:
            if not self.fields or not self.contexts or len(self.fields) != len(self.contexts):
                raise ConfigurationError(
                    f"For domain {self.name} fields and contexts numbers are not equal"
                )


@dataclass
class KnowledgeConfigRequiredParameters:
    feature_name: str
    field: str
    domain: Domain
    methods: ClassVar[Dict[str, Callable]] = {}

    @classmethod
    def prepare(cls, domain: Domain, field: str) -> "KnowledgeConfigRequiredParameters":
        return KnowledgeConfigRequiredParameters(
            feature_name=f"{domain.name}_{field}", field=field, domain=domain
        )


def get_comment(instance, comment_type):
    return getattr(instance.comments, comment_type)


def post_init(instance):
    instance.comments = SentimentCommentConfig(**instance.comments)


@dataclass
class MeasureConfigRequiredParameters:
    feature_name: str
    field: str
    context: str
    comments: Dict[str, Any]
    domain: Domain
    methods: ClassVar[Dict[str, Callable]] = {
        "__post_init__": post_init,
        "get_comment": get_comment,
    }

    @classmethod
    def prepare(
        cls, domain: Domain, context: str, field: str, comments: Dict[str, Any]
    ) -> "MeasureConfigRequiredParameters":
        return MeasureConfigRequiredParameters(
            feature_name=f"{domain.name}_{context}",
            field=field,
            context=context,
            comments=comments,
            domain=domain,
        )


class Toolbox:
    def __init__(self):
        self.standard_features_configs = {}
        self.custom_features = {}


ConfigRequiredParameters = Union[KnowledgeConfigRequiredParameters, MeasureConfigRequiredParameters]
