import xml.etree.ElementTree as ET
from dataclasses import dataclass


@dataclass
class Item:
    child: ET.Element
    value: str
    parsed_item: list


def get_all_ancestors(child, ancestors, parent_map):
    ancestors.append(child)
    try:
        parent = parent_map[child]
    except KeyError:
        return None

    return get_all_ancestors(parent, ancestors, parent_map)


def present_ancestors(ancestors):
    new_ancestors = []
    for ancestor in ancestors:
        keys_values_ancestors = []
        for key, value in ancestor.attrib.items():
            keys_values_ancestors.append(f"{key}:{value}")
        keys_values_ancestors = "|".join(keys_values_ancestors)
        if keys_values_ancestors:
            new_ancestors.append(f"<{ancestor.tag} {keys_values_ancestors}>")
        else:
            new_ancestors.append(f"<{ancestor.tag}>")

    ancestors = new_ancestors[::-1]
    return "".join(ancestors)


def get_features(xml_tree, func, print_results):
    parent_map = {c: p for p in xml_tree.iter() for c in p}
    features = []

    for child in xml_tree.iter():
        if child.text:
            parsed_value = func(child.text)
            if parsed_value:
                ancestors = []
                get_all_ancestors(child, ancestors, parent_map)
                ancestors = present_ancestors(ancestors)
                features.append(Item(ancestors, child.text, parsed_value))
    print_results(features)
    return features


def get_column_features(column_name, value, func, print_results):
    if not value:
        return Item(child=column_name, value=value, parsed_item="")
    parsed_value = func(value)
    if parsed_value:
        print_results(column_name, value, parsed_value)
    return Item(child=column_name, value=value, parsed_item=value)


def print_org_results(org_features):
    for org_list in org_features:
        print("tag name ", org_list.child)
        print("value", org_list.value)
        for org in org_list.parsed_item:
            print("base_names: ", org.base.cleaned_name)
            print("legal: ", org.legal.cleaned_name)
            print()


def print_column_results(column_name, value, parsed_value):
    print(f"column {column_name}:")
    print(f"value {value}:")
    for i in parsed_value:
        print("base_names: ", i.base.cleaned_name)
        print("legal: ", i.legal.cleaned_name)
        print()
