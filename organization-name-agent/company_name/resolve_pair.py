import yaml
from company_name.compare import compare
from company_name.score_eval import evaluate_scores
from company_name.comment_generator import generate_comment


if __name__ == '__main__':
    with open('config.yaml') as config_file:
        cfg = yaml.load(config_file, Loader=yaml.FullLoader)

    scores, features = compare('EN BANK PJSC', 'RAIFFEISENBANK')
    evals = evaluate_scores(scores, cfg['features'])
    comment = generate_comment(features, evals, cfg['features'])
    print(comment)
