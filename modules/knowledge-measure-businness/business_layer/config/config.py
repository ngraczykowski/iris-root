from dataclasses import asdict, make_dataclass
from importlib import import_module
from typing import Any, Dict, Set, TypeVar

from business_layer.api import BaseCustomFeatureType
from business_layer.config.datatypes import (
    Comment,
    ConfigField,
    ConfigRequiredParameters,
    Domain,
    DomainSource,
    DomainType,
    KnowledgeConfigRequiredParameters,
    MeasureConfigRequiredParameters,
    Toolbox,
)
from business_layer.errors import ConfigurationError

ConfigType = TypeVar("ConfigType")


class DomainConfigFactory:
    def __init__(
        self,
        measure_toolbox: Toolbox,
        knowledge_toolbox: Toolbox,
        custom_features_config: Dict[str, Any],
    ):
        self.custom_features_config = custom_features_config
        self.knowledge_toolbox = knowledge_toolbox
        self.measure_toolbox = measure_toolbox

    @staticmethod
    def format_name(name: str) -> str:
        return name.title().replace("_", "")

    def create_config(self, params: ConfigRequiredParameters) -> Dict[str, ConfigType]:
        feature_name = params.feature_name
        dataclass_type = make_dataclass(
            f"{self.format_name(feature_name)}Config",
            asdict(params).keys(),
            namespace=params.methods,
        )
        feature_config = {feature_name: dataclass_type(**asdict(params))}
        return feature_config

    def initialize_custom_tool(
        self, domain_name: str, params: ConfigRequiredParameters, domain_type: DomainType
    ) -> Dict[str, BaseCustomFeatureType]:
        module_name = f"business_layer.custom_{domain_type.value}.{domain_name}"
        try:
            module = import_module(module_name)
        except ModuleNotFoundError:
            raise ConfigurationError(f"Cannot import required module {module_name}")

        try:
            custom_tool = getattr(module, self.format_name(domain_name))
            return custom_tool(params)
        except AttributeError:
            raise ConfigurationError(
                f"Cannot find a {self.format_name(domain_name)} class in {module_name}"
            )

    def update_mappings(
        self,
        params: ConfigRequiredParameters,
        domain: Domain,
        toolbox: Toolbox,
    ):
        final_config = self.create_config(params)
        if domain.source == DomainSource.CUSTOM_FEATURES:
            toolbox.custom_features[params.feature_name] = self.initialize_custom_tool(
                domain.name, params, domain.type_name
            )
            self.custom_features_config.update(final_config)
        elif domain.source == DomainSource.STANDARD_FEATURES:
            toolbox.standard_features_configs.update(final_config)

        else:
            raise ConfigurationError(f"Invalid config for domain {domain.name}!")

    def initialize_feature(self, domain: Domain):
        if domain.type_name == DomainType.KNOWLEDGE:
            for field in domain.fields:
                params = KnowledgeConfigRequiredParameters.prepare(domain, field)
                self.update_mappings(
                    params,
                    domain,
                    self.knowledge_toolbox,
                )
        elif domain.type_name == DomainType.MEASURE:
            for field, context in zip(domain.fields, domain.contexts):
                params = MeasureConfigRequiredParameters.prepare(
                    domain, context, field, domain.comments
                )
                self.update_mappings(
                    params,
                    domain,
                    self.measure_toolbox,
                )

        else:
            raise ConfigurationError(f"Invalid config for domain {domain.name}!")


class Config:
    def __init__(self, config_parameters: Dict[str, Any]):
        try:
            self.comment_intro: str = config_parameters[ConfigField.COMMENT_INTRO.value]
            self.decisions: Set[str] = set(config_parameters[ConfigField.DECISIONS.value].keys())
            self.comments: Dict[str, Comment] = {
                decision: Comment(
                    verbal=comment[ConfigField.DECISIONS_DETAILS_VERBAL.value],
                    type=comment[ConfigField.DECISIONS_DETAILS_COMMENT_TYPE.value],
                )
                for decision, comment in config_parameters[ConfigField.DECISIONS.value].items()
            }
            feature_mappings = config_parameters[ConfigField.FEATURE_MAPPING.value]
        except (KeyError, TypeError):
            raise ConfigurationError("Invalid configuration file content!")

        self.knowledge_toolbox = Toolbox()
        self.measure_toolbox = Toolbox()
        custom_features_config = {}
        self.produce_domains(feature_mappings, custom_features_config)
        self.requested_mappings = dict(
            **self.knowledge_toolbox.standard_features_configs,
            **self.measure_toolbox.standard_features_configs,
            **custom_features_config,
        )

    def produce_domains(
        self, feature_mappings: Dict[Any, Any], custom_features_config: Dict[Any, Any]
    ):
        config_factory = DomainConfigFactory(
            knowledge_toolbox=self.knowledge_toolbox,
            measure_toolbox=self.measure_toolbox,
            custom_features_config=custom_features_config,
        )
        for domain_source, domain in feature_mappings.items():
            for domain_name, domain_type in domain.items():
                for domain_type_name, domain_config in domain_type.items():
                    try:
                        if domain_type_name == DomainType.KNOWLEDGE.value:
                            contexts, comments = None, None
                            ignore = False
                        elif domain_type_name == DomainType.MEASURE.value:
                            contexts = domain_config[ConfigField.DOMAIN_CONFIG_CONTEXTS.value]
                            comments = domain_config[ConfigField.DOMAIN_CONFIG_COMMENTS.value]
                            ignore = domain_config.get(
                                ConfigField.DOMAIN_CONFIG_IGNORE.value, False
                            )
                        else:
                            raise ConfigurationError(f"Invalid config for domain {domain_name}!")
                        domain_info = Domain(
                            name=domain_name,
                            source=domain_source,
                            type_name=domain_type_name,
                            fields=domain_config[ConfigField.DOMAIN_CONFIG_FIELDS.value],
                            contexts=contexts,
                            comments=comments,
                            ignore=bool(ignore),
                        )
                        config_factory.initialize_feature(domain_info)
                    except KeyError:
                        raise ConfigurationError("Invalid configuration file content!")
