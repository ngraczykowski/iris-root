import yaml, json, pickle
import numpy as np
from company_name.compare import compare
from company_name.score_eval import evaluate_scores
from company_name.scores.score import Score
from sklearn.linear_model import LogisticRegression

class CompanyNameModel:
    def __init__(self,
                 score_config_path: str,
                 sklearn_model_path: str,
                 no_match_threshold: float,
                 business_rules: dict = None
                ):
        self.score_config_path = score_config_path
        self.sklearn_model_path = sklearn_model_path
        self.no_match_threshold = no_match_threshold if no_match_threshold else 0.87
        self.business_rules = business_rules
        
        self._read_model()
        
    def __repr__(self):
         return (
            "SkleanModel("
            + ", \n".join(
                f"{key}={getattr(self, key)!r}" for key in self.__dict__.keys()
            )
            + ")"
        )
        
    def _read_model(self):
        score_config_file = open(self.score_config_path)
        self.score_config = yaml.load(score_config_file, Loader=yaml.FullLoader)
        score_config_file.close()
        
        sklearn_model = pickle.load(open(self.sklearn_model_path, 'rb'))
        self.lb_classes = sklearn_model['lb_classes']
        self.clf = sklearn_model['clf']
        self.imputed_vals = sklearn_model['imputed_vals']
        
    def _resolve_pair(self, name1, name2):
        try:
            scores = compare(name1, name2)
            evals = evaluate_scores(scores, self.score_config['features'])
            return scores, evals
        except:
            print(f'Unable to handle name1 {name1}, name2 {name2}')
        
    def _create_feature_vector(self, resolve_pair_result):
        fv = []
        for k, v in resolve_pair_result[0].items():
            if v.status != Score.ScoreStatus.OK:
                imputed_value = self.imputed_vals[k+'_value']
                fv.append(imputed_value)
            else:
                fv.append(v.value)

        return fv
    
    def _apply_business_rule(self, fv):
        def __get_feature_index(feature):
            return list(self.imputed_vals.keys()).index(feature)
        
        rules_dict = dict()
        if self.business_rules:
            for k, v in self.business_rules.items():
                feature_index = __get_feature_index(k)
                rules_dict[feature_index] = v
                
        for k, v in rules_dict.items():
            if fv[k] >= v:
                return 'MATCH'
            
    def get_fv(self, ap_name, wl_name):
        resolve_pair_result = self._resolve_pair(ap_name, wl_name)
        fv = self._create_feature_vector(resolve_pair_result)
        return fv
    
    def get_probs(self, fv):
        return self.clf.predict_proba(np.array(fv).reshape(1, -1))    
        
    def predict(self, ap_name, wl_name):
        fv = self.get_fv(ap_name, wl_name)
        if self._apply_business_rule(fv) == 'MATCH':
            return 'MATCH'
        
        probs = self.get_probs(fv)
        no_match_idx = list(self.lb_classes).index('NO_MATCH')
        if probs[0, no_match_idx] >= self.no_match_threshold:
            return 'NO_MATCH'
        else:
            return 'MATCH'
        
if __name__ == '__main__':
    config_file = open('company_name/config/model-config.yaml')
    model_config = yaml.load(config_file, Loader=yaml.FullLoader)
    config_file.close()
    
    company_name_model = CompanyNameModel(model_config['score_config_path'],
                                      model_config['sklearn_model_path'],
                                      model_config['no_match_threshold'],
                                      model_config['business_rules']
                                     )
    
    print('Expected NO_MATCH, actual', company_name_model.predict('MANACO sdf ENTERPRISE PTE LIMITD', 'WT MARINE ENTERPRISE PTE LTD'))
    
    # Use the fv from the above example, but changed the last element to 1 to allow business rules kick in
    print('Expected MATCH, actual', company_name_model._apply_business_rule([0.03807615230469464,
                                             0,
                                             0.35,
                                             1.0,
                                             0.7,
                                             0.67,
                                             0.73,
                                             0.75,
                                             0.2,
                                             1,
                                             0.0,
                                             0.9481132075504629,
                                             0.4,
                                             0.73,
                                             1.0,
                                             0.2980074828156059,
                                             1.0])
         )