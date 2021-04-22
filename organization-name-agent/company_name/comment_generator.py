from company_name.score_outcomes import Outcomes


def format_contents(compared):
    frmt_contents = []
    for content in compared:
        frmt_content = ' '.join(content) if content else '<NO_DATA>'
        frmt_contents.append(frmt_content)
    return frmt_contents


def blacklist_comment(company_name):
    return f'{company_name} has been blacklisted'


def generic_comment(outcome, frmt_contents, feature_desc):
    ap_features, wl_features = frmt_contents
    return f'{outcome.value} on {feature_desc} ({ap_features} vs. {wl_features})'


def generate_comment(ap_name, evals: dict, feature_cfg: dict):
    comment = []

    for feature, evaluation in evals.items():
        outcome = evaluation[0]
        compared = evaluation[1]
        frmt_contents = format_contents(compared)

        if outcome in [Outcomes.NO_DATA, Outcomes.DISABLED]:
            continue
        elif feature == 'blacklisted' and outcome == Outcomes.MATCH:
            return blacklist_comment(ap_name)
        elif feature == 'single_token':
            if outcome == Outcomes.MATCH:
                return generic_comment(outcome, frmt_contents, feature_cfg[feature]['desc'])
            else:
                continue
        elif feature == 'potential_subsidiary' and outcome == Outcomes.MATCH:
            return generic_comment(outcome, frmt_contents, feature_cfg[feature]['desc'])

        comment.append(generic_comment(outcome, frmt_contents, feature_cfg[feature]['desc']))
    return '. '.join(comment)
