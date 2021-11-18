from typing import Dict, Set


def cut_name_to_leftmost_match(name: str, matches: Set[str]) -> str:
    def get_match_last_index(match: Dict):
        return 1 + match["idx"] + len(match["match"])  # 1 because of leading " " cutting

    matches = filter(lambda match: match in name, matches)  # to avoid using no-matches
    match_to_index = [{"match": match, "idx": name.find(" " + match)} for match in matches]

    if not match_to_index:
        return name

    sorted_matches = sorted(match_to_index, key=lambda x: (x["idx"], len(x["match"])))

    if len(sorted_matches) == 1:
        leftmost_legal_sequence_last_char_index = get_match_last_index(sorted_matches[0])

    # two consecutive legal terms - take the last index of the last one
    elif sorted_matches[1]["idx"] - get_match_last_index(sorted_matches[0]) <= 2:
        leftmost_legal_sequence_last_char_index = get_match_last_index(sorted_matches[1])

    else:
        leftmost_legal_sequence_last_char_index = get_match_last_index(sorted_matches[0])

    return name[:leftmost_legal_sequence_last_char_index]
