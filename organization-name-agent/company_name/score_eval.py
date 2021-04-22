from company_name.score_outcomes import Outcomes


def evaluate_scores(scores, feature_cfg):
    evals = {}
    for feature, params in feature_cfg.items():
        score_obj = scores[params['score_name']]

        if not params['active']:
            outcome = Outcomes.DISABLED
        elif not score_obj.compared[0] and not score_obj.compared[1]:
            outcome = Outcomes.NO_DATA
        elif scores[params['score_name']].value < params['low_thr']:
            outcome = Outcomes.NO_MATCH
        elif scores[params['score_name']].value < params['high_thr']:
            outcome = Outcomes.PARTIAL_MATCH
        else:
            outcome = Outcomes.MATCH
            if feature == 'potential_subsidiary' and scores['absolute_tokenization'].value < 2:
                outcome = Outcomes.NO_MATCH

        evals[feature] = (outcome, score_obj.compared)
    return evals
