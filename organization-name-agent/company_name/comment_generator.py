def format_feature(features, feature):
    frmt_features = []
    for f in features:
        frmt_features.append(' '.join(f[feature]).upper() if f[feature] else '<NO_DATA>')
    return frmt_features


def comment_on_feature(evaluation, frmt_features, feature_desc):
    ap_features, wl_features = frmt_features
    return f'{evaluation.value} on {feature_desc} ({ap_features} vs. {wl_features})'


def generate_comment(features, evals, feature_cfg):
    comment = []
    for feature, evaluation in evals.items():
        frmt_features = format_feature(features, feature)
        comment.append(comment_on_feature(evaluation, frmt_features, feature_cfg[feature]['desc']))
    return '. '.join(comment)
