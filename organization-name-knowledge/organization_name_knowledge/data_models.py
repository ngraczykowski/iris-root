import dataclasses

from organization_name_knowledge import NameInformation


@dataclasses.dataclass
class OriginalNameInformation:
    source: str
    common_prefixes: str
    base: str
    common_suffixes: str
    legal: str
    countries: str
    parenthesis: str
    other: str

    @classmethod
    def from_NameInformation(cls, name_information: NameInformation) -> "OriginalNameInformation":
        return cls(
            source=name_information.source.original,
            common_prefixes=name_information.common_prefixes.original_name,
            base=name_information.base.original_name,
            common_suffixes=name_information.common_suffixes.original_name,
            legal=name_information.legal.original_name,
            countries=name_information.countries.original_name,
            parenthesis=name_information.parenthesis.original_name,
            other=name_information.other.original_name,
        )
