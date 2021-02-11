import numpy
import pandas
from sklearn.metrics import recall_score, precision_score, accuracy_score
from sklearn.linear_model import LogisticRegressionCV, LogisticRegression


df = pandas.read_csv("./data/company_pairs_with_results.csv")


def correlation():
    names = ['abbreviation', 'fuzzy', 'partial_fuzzy', 'sorted_fuzzy', 'legal_terms', 'tokenization']

    print(names)
    print(numpy.corrcoef([df[name] for name in names]))


def logistic_regression():
    names = ['abbreviation', 'partial_fuzzy', 'sorted_fuzzy', 'legal_terms', 'tokenization']
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
logistic_regression()
