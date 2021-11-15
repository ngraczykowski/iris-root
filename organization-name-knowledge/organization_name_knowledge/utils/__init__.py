from typing import Set


def cut_name_to_leftmost_match(name: str, matches: Set[str]) -> str:
    matches = filter(lambda match: match in name, matches)  # to avoid using no-matches
    match_to_index = [{"match": match, "idx": name.find(match)} for match in matches]
    leftmost_one = min(match_to_index, key=lambda x: x["idx"])
    cut_name = name[: leftmost_one["idx"] + len(leftmost_one["match"])]
    return cut_name
