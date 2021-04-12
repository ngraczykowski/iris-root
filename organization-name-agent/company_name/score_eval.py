from company_name.score_outcomes import Outcomes


def evaluate_scores(scores, feature_cfg):
    evals = {}
    for feature, params in feature_cfg.items():
        if scores[params['score_name']] < params['low_thr']:
            evals[feature] = Outcomes.NO_MATCH
        elif scores['score_name'] < params['high_thr']:
            evals[feature] = Outcomes.PARTIAL_MATCH
        else:
            evals[feature] = Outcomes.MATCH
    return evals
