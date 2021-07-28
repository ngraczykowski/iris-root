import collections
import csv
import itertools
import multiprocessing
import random
from typing import Any, List, Tuple

import numpy
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


COMPANY_FILE = "../data/company_pairs_with_results.csv"
with open(COMPANY_FILE, "rt") as f:
    companies_columns = {
        name: i for i, name in enumerate(next(f).rstrip().split(",")[2:])
    }
companies_df = numpy.genfromtxt(COMPANY_FILE, delimiter=",")[1:].transpose()[2:]

Score = collections.namedtuple("Score", ("accuracy", "precision", "recall"))


def get_threshold_score(thresholds):
    correct = []
    for i, threshold in enumerate(thresholds):
        if threshold is None:
            continue
        keys = scores_all_keys[i]
        value = sum(companies_df[companies_columns[key]] for key in keys)
        correct.append((value >= threshold))
    result = numpy.any(correct, axis=0)

    i = companies_columns["is_similar"]
    tp = ((companies_df[i] == 1) & result).sum()
    tn = ((companies_df[i] == 0) & ~result).sum()

    fp = ((companies_df[i] == 0) & result).sum()
    fn = ((companies_df[i] == 1) & ~result).sum()

    accuracy = (tp + tn) / (tp + tn + fp + fn)
    precision = tp / (tp + fp)
    recall = tp / (tp + fn)
    return thresholds, Score(accuracy, precision, recall)


def get_thresholds(set_keys, keys):
    if not keys:
        yield set_keys
        return

    for i in [0.10, 0.20, 0.30, 0.40, 0.50, 0.6, 0.70, 0.8, 0.90, 1, 1.1]:
        yield from get_thresholds((*set_keys, i), keys[1:])


def print_top(result: List[Tuple[Any, Score]], k=3):
    for t in ("accuracy", "precision", "recall"):
        sorted_list = sorted(((getattr(r[1], t), r) for r in result), reverse=True)
        print(t, ":")
        for _, (_, r) in zip(range(k), sorted_list):
            print("\t", r)

    sorted_list = sorted(((sum(list(r[1])), r) for r in result), reverse=True)
    print("sum:")
    for _, (_, r) in zip(range(k), sorted_list):
        print("\t", r)

    print()


def main():
    pool = multiprocessing.Pool(processes=10)
    thresholds = list(get_thresholds((), list(scores_all_keys[:7])))
    random.shuffle(thresholds)
    print(len(thresholds))
    result = []
    for i in range(1, len(thresholds), 5000):
        this_thresholds = thresholds[i : i + 5000]
        for r in pool.imap(get_threshold_score, this_thresholds, 100):
            result.append(r)
        print_top(result)

        with open("../data/thresholds_scores.csv", "wt") as f:
            writer = csv.writer(f)
            writer.writerow(
                ([*[k[0] for k in scores_all_keys], "accuracy", "precision", "recall"])
            )
            for r in result:
                writer.writerow([*r[0], *r[1]])


if __name__ == "__main__":
    # compute_scores()
    main()
