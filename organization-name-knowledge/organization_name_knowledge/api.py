from typing import List, Set

from organization_name_knowledge.freetext.matching import get_all_contained_legal_terms
from organization_name_knowledge.freetext.parse import parse_freetext_names
from organization_name_knowledge.names.name_information import NameInformation
from organization_name_knowledge.names.parse import parse_name


def parse(name: str) -> NameInformation:
    """Parse organization name, to extract base of name, and its surrounding:
    legal terms, country names, prefixes, suffixes, parentheses.
    For details, check out *NameInformation* class docstrings.

    Parameters
    ----------
    name : str
        In most of use cases it is an organization name

    Returns
    -------
    NameInformation
        NameInformation class object
    """

    name_information = parse_name(name)
    return name_information


def parse_freetext(
    freetext: str,
    base_tokens_upper_limit: int = 3,
    name_tokens_lower_limit: int = 2,
    name_tokens_upper_limit: int = 7,
) -> List[NameInformation]:
    """Parse freetext to find each organization name that is present within passed text.
    There are tokens limits with default values provided. It is not recommended to change these values,
    but in specific situation there is a possibility to apply another limits by changing them.

    Parameters
    ----------
    freetext : str
        Any string which contains or not, an organization name(s)
    base_tokens_upper_limit : int
        The maximum number of tokens in parsed organization name base
    name_tokens_lower_limit : int
        The minimum number of tokens in whole parsed organization name
    name_tokens_upper_limit: int
        The maximum number of tokens in whole parsed organization name
    Returns
    -------
    List[NameInformation]
        A list of found and parsed organization names, as NameInformation objects
    """

    names: List[NameInformation] = parse_freetext_names(
        freetext, base_tokens_upper_limit, name_tokens_lower_limit, name_tokens_upper_limit
    )
    return sorted(set(names), key=lambda name: name.base.cleaned_name)


def get_all_legal_terms(name: str) -> Set[str]:
    """Legal terms returned by this function are slightly different than those returned by main
    "parse" function - they may come from i.e. part of the name, or from inside the name string.
    In other words, they are all of the substrings that match any of known legal terms.

    Parameters
    ----------
    name : str
        In most of use cases it is an organization name

    Returns
    -------
    Set[str]
        Set of name substrings that match any of known legal terms
    """
    return get_all_contained_legal_terms(name)
