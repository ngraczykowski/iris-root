from company_name.score_outcomes import Outcomes


def evaluate_scores(scores, feature_cfg):
    evals = {}
    for feature, params in feature_cfg.items():
        score_obj = scores[params['score_name']]
        if not score_obj.compared[0] and not score_obj.compared[1]:
            continue

        if scores[params['score_name']].value < params['low_thr']:
            evals[feature] = Outcomes.NO_MATCH
        elif scores[params['score_name']].value < params['high_thr']:
            evals[feature] = Outcomes.PARTIAL_MATCH
        else:
            evals[feature] = Outcomes.MATCH

        evals[feature] = (evals[feature], score_obj.compared)
    return evals
