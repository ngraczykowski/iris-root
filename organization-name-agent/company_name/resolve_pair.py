import yaml
from company_name.compare import compare
from company_name.score_eval import evaluate_scores
from company_name.comment_generator import generate_comment


def resolve_pair(name1, name2):
    scores = compare(name1, name2)
    print('SCORES\n', scores)

    evals = evaluate_scores(scores, cfg['features'])
    print('EVALUATIONS\n', evals)

    comment = generate_comment(name1, evals, cfg['features'])
    print('COMMENT\n', comment)
    print()


if __name__ == '__main__':
    with open('config.yaml') as config_file:
        cfg = yaml.load(config_file, Loader=yaml.FullLoader)

    pairs = [
        ('EN BANK PJSC', 'RAIFFEISENBANK'),
        ('BANK VTB 24 CJSC RE FX SPOT', 'VTB REGISTRAR CJSC'),
        ('UNIVERSAL ISLAMIC MEAT FZCO', 'UNIVERSAL'),
        ('SINO OCEAN TRADING LTD', 'SINO OCEAN SHIPPING CO LTD'),
        ('GRAND ENTERPRISES GROUP LIMITED', 'GROUP GRAND LIMITED'),
        ('Jiangling Motors Corporation Limited', 'JMC'),
    ]
    for pair in pairs:
        resolve_pair(*pair)
