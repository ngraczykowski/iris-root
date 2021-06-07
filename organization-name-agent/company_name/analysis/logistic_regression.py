import numpy
import pandas
from sklearn.metrics import recall_score, precision_score, accuracy_score
from sklearn.linear_model import LogisticRegression


df = pandas.read_csv("../data/company_pairs_with_results.csv")


def correlation():
    names = [
        "abbreviation",
        "fuzzy",
        "partial_fuzzy",
        "sorted_fuzzy",
        "legal_terms",
        "tokenization",
    ]

    print(names)
    print(numpy.corrcoef([df[name] for name in names]))


def logistic_regression():
    names = [
        "abbreviation",
        "partial_fuzzy",
        "sorted_fuzzy",
        "legal_terms",
        "tokenization",
    ]
    X = df[names]
    y = df["is_similar"]

    clf = LogisticRegression().fit(X, y)

    print("bias", clf.intercept_)
    print("coefficients")
    print(names)
    print(clf.coef_)

    predicted = clf.predict(X)
    print("accuracy", accuracy_score(y, predicted))
    print("precision", precision_score(y, predicted))
    print("recall", recall_score(y, predicted))


correlation()
"""
['abbreviation', 'fuzzy', 'partial_fuzzy', 'sorted_fuzzy', 'legal_terms', 'tokenization']
[[ 1.         -0.08499141  0.01861029 -0.12704623  0.12502459 -0.06971118]
 [-0.08499141  1.          0.88226062  0.97431136  0.28305059  0.77884516]
 [ 0.01861029  0.88226062  1.          0.85666572  0.27427395  0.73454346]
 [-0.12704623  0.97431136  0.85666572  1.          0.26903705  0.77676907]
 [ 0.12502459  0.28305059  0.27427395  0.26903705  1.          0.21394319]
 [-0.06971118  0.77884516  0.73454346  0.77676907  0.21394319  1.        ]]
"""

logistic_regression()
"""
bias [-10.8942911]
coefficients
['abbreviation', 'partial_fuzzy', 'sorted_fuzzy', 'legal_terms', 'tokenization']
[[13.89299813  8.36635111  4.68482265  3.97434288  4.46115847]]
accuracy 0.9816993649689112
precision 0.9824731613692447
recall 0.938831448898563
"""
