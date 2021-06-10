INSERT INTO ae_alert
VALUES (1, 'alert-1', now(), now(), 1)
     , (2, 'alert-1', now(), now(), 1)
;

INSERT INTO ae_match
VALUES (11, 1, now(), 'alert-1-match-1', 1)
     , (12, 1, now(), 'alert-1-match-2', 2)
     , (21, 2, now(), 'alert-2-match-1', 1)
;

INSERT INTO ae_analysis
VALUES (1, 'policies/asd', 'strategy/asd', now(), 'NEW')
     , (2, 'policies/asd', 'strategy/asd', now(), 'NEW')
;

INSERT INTO ae_pending_recommendation
VALUES (1, 1, now())
     , (2, 1, now())
;

INSERT INTO ae_category (category_id, created_at, category)
VALUES (1, now(), 'categories/country')
     , (2, now(), 'categories/ser')
;
INSERT INTO ae_analysis_category (analysis_category_id, analysis_id, category_id)
VALUES (1, 1, 1)
     , (2, 1, 2)
;
INSERT INTO ae_agent_config_feature
VALUES (1, now(), 'agents/document/versions/2.1.0/configs/1', 'features/passport');
INSERT INTO ae_analysis_feature (analysis_feature_id, analysis_id, agent_config_feature_id)
VALUES (1, 1, 1);
