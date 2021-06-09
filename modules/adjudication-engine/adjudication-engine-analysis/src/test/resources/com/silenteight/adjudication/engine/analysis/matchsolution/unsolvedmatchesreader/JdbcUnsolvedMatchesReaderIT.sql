-- Categories
INSERT INTO ae_category (category_id, created_at, category)
VALUES (1, now(), 'categories/hit_type'),
       (2, now(), 'categories/customer_type')
;

-- Features
INSERT INTO ae_agent_config_feature(agent_config_feature_id, created_at, agent_config,
                                    feature)
VALUES (1, now(), 'agents/document/versions/2.1.0/configs/1', 'features/national_id'),
       (2, now(), 'agents/document/versions/2.1.0/configs/1', 'features/passport'),
       (3, now(), 'agents/name/versions/3.3.0/configs/1', 'features/name')
;

-- Alerts
INSERT INTO ae_alert (alert_id,
                      client_alert_identifier,
                      created_at,
                      alerted_at,
                      priority)
VALUES (1, 'alert-1', now(), now(), 5),
       (2, 'alert-2', now(), now(), 5)
;

-- Matches
INSERT INTO ae_match (match_id, alert_id, created_at, client_match_identifier, sort_index)
VALUES (11, 1, now(), 'match-11', 1),
       (12, 1, now(), 'match-12', 2),
       (21, 2, now(), 'match-21', 1),
       (22, 2, now(), 'match-22', 2),
       (23, 2, now(), 'match-23', 3)
;

-- Dataset
INSERT INTO ae_dataset (dataset_id, created_at)
VALUES (1, now());
INSERT INTO ae_dataset_alert (dataset_id, alert_id)
VALUES (1, 1),
       (1, 2)
;

-- COMMIT;

-- Analysis
INSERT INTO ae_analysis (analysis_id, policy, strategy, created_at, state)
VALUES (1, 'policies/0864f110-8381-4e8c-b8f4-4eac6a53c409', 'strategies/back_test', now(), 'NEW');
INSERT INTO ae_analysis_category (analysis_category_id, analysis_id, category_id)
VALUES (11, 1, 1),
       (12, 1, 2)
;
INSERT INTO ae_analysis_feature (analysis_feature_id, analysis_id, agent_config_feature_id)
VALUES (11, 1, 1),
       (12, 1, 2),
       (13, 1, 3)
;
INSERT INTO ae_analysis_dataset (analysis_id, dataset_id)
VALUES (1, 1);

REFRESH MATERIALIZED VIEW ae_analysis_alert_query;

-- Pending recommendations
INSERT INTO ae_pending_recommendation (analysis_id, alert_id, created_at)
VALUES (1, 1, now()),
       (1, 2, now())
;

-- COMMIT;

-- Category Values
INSERT INTO ae_match_category_value (match_id, category_id, created_at, value)
VALUES (11, 1, now(), 'DENY'),
       (11, 2, now(), 'C'),
       (21, 1, now(), 'PEPL'),
       (22, 1, now(), 'PEPL'),
       (22, 2, now(), 'I')
;

-- Feature Values
INSERT INTO ae_match_feature_value (match_id, agent_config_feature_id, created_at, value, reason)
VALUES (11, 1, now(), 'NID_NO_MATCH', '{}'),
       (11, 2, now(), 'DOC_MATCH', '{}'),
       (11, 3, now(), 'NAM_EXACT_MATCH', '{}'),
       (21, 1, now(), 'NID_NO_MATCH', '{}'),
       (21, 2, now(), 'DOC_MATCH', '{}'),
       (21, 3, now(), 'NAM_EXACT_MATCH', '{}'),
       (22, 1, now(), 'NID_NO_MATCH', '{}'),
       (22, 2, now(), 'DOC_MATCH', '{}')
;
