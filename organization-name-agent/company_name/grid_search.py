import collections
import itertools
import json
import random
import multiprocessing
import csv

import pandas

from typing import *

from compare import score


scores_keys = {
    "abbreviation",
    "fuzzy",
    "partial_fuzzy",
    "sorted_fuzzy",
    "tokenization",
}

scores_all_keys: List[Tuple[str, ...]] = [(k,) for k in scores_keys]
a = [(k1, k2) for k1, k2 in itertools.product(scores_keys, scores_keys) if k1 < k2]


def compute_scores(
    source_file="./data/train_company_pairs.csv",
    dest_file="./data/company_pairs_with_results_2.csv",
):
    with open(source_file, "rt") as f:
        with open(dest_file, "wt") as out_f:
            writer = csv.DictWriter(
                out_f,
                fieldnames=(
                    "name1",
                    "name2",
                    "is_similar",
                    *scores_keys,
                    "parenthesis_match",
                    "legal_terms",
                ),
            )
            writer.writeheader()
            for row in csv.DictReader(f):
                scored = score(row["name1"], row["name2"])
                writer.writerow({**row, **scored})


def get_companies(source):
    return pandas.read_csv(source)


companies_df = get_companies("./data/company_pairs_with_results.csv")

Score = collections.namedtuple("Score", ("accuracy", "precision", "recall"))


def get_threshold_score(thresholds):
    correct = []
    for keys, threshold in thresholds:
        value = sum(companies_df[key] for key in keys)
        correct.append((value >= threshold))
    result = pandas.DataFrame(correct).any()

    tp = (companies_df["is_similar"] == 1 & result).sum()
    tn = (companies_df["is_similar"] == 0 & ~result).sum()

    fp = (companies_df["is_similar"] == 0 & result).sum()
    fn = (companies_df["is_similar"] == 1 & ~result).sum()

    accuracy = (tp + tn) / (tp + tn + fp + fn)
    precision = tp / (tp + fp)
    recall = tp / (tp + fn)
    return thresholds, Score(accuracy, precision, recall)


def get_thresholds(set_keys, keys):
    if not keys:
        yield set_keys
        return

    for i in range(40, 101, 10):
        yield from get_thresholds((*set_keys, (keys[0], i / 100)), keys[1:])
    yield from get_thresholds(set_keys, keys[1:])


def print_top(result: List[Tuple[Any, Score]], k=3):
    for t in ("accuracy", "precision", "recall"):
        sorted_list = reversed(sorted((getattr(r[1], t), r) for r in result))
        print(t, ":")
        for i, (_, r) in zip(range(k), sorted_list):
            print("\t", r)

    sorted_list = reversed(sorted((sum(list(r[1])), r) for r in result))
    print("sum:")
    for i, (_, r) in zip(range(k), sorted_list):
        print("\t", r)

    print()


def main():
    pool = multiprocessing.Pool(processes=10)
    thresholds = list(get_thresholds((), list(scores_all_keys[:7])))
    random.shuffle(thresholds)
    print(len(thresholds))
    result = []
    for i in range(1, len(thresholds), 50):
        this_thresholds = thresholds[i : i + 50]
        for r in pool.imap(get_threshold_score, this_thresholds):
            result.append(r)
        print_top(result)
        with open("./data/thresholds_scores.json", "wt") as f:
            json.dump(result, f, indent=2)
        break


if __name__ == "__main__":
    #compute_scores()
    main()
