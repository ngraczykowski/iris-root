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
       (2, 'alert-2', now(), now(), 5),
       (3, 'alert-3', now(), now(), 5),
       (4, 'alert-4', now(), now(), 5)
;

-- Matches
INSERT INTO ae_match (match_id, alert_id, created_at, client_match_identifier, sort_index)
VALUES (11, 1, now(), 'match-11', 1),
       (12, 1, now(), 'match-12', 2),
       (21, 2, now(), 'match-21', 1),
       (22, 2, now(), 'match-22', 2),
       (23, 2, now(), 'match-23', 3),
       (31, 3, now(), 'match-31', 1),
       (32, 3, now(), 'match-32', 2),
       (33, 3, now(), 'match-33', 3),
       (34, 3, now(), 'match-34', 4),
       (35, 3, now(), 'match-35', 5),
       (36, 3, now(), 'match-36', 6),
       (37, 3, now(), 'match-37', 7),
       (38, 3, now(), 'match-38', 8),
       (41, 4, now(), 'match-41', 1),
       (42, 4, now(), 'match-42', 2),
       (43, 4, now(), 'match-43', 3)
;

-- Dataset
INSERT INTO ae_dataset (dataset_id, created_at)
VALUES (1, now()),
       (2, now())
;
INSERT INTO ae_dataset_alert (dataset_id, alert_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4)
;

-- COMMIT;

-- Analysis
INSERT INTO ae_analysis (analysis_id, policy, strategy, created_at)
VALUES (1, 'policies/0864f110-8381-4e8c-b8f4-4eac6a53c409', 'strategies/back_test', now()),
       (2, 'policies/0864f110-8381-4e8c-b8f4-4eac6a53c409', 'strategies/back_test', now()),
       (3, 'policies/0864f110-8381-4e8c-b8f4-4eac6a53c409', 'strategies/back_test', now()),
       (4, 'policies/0864f110-8381-4e8c-b8f4-4eac6a53c409', 'strategies/back_test', now());
INSERT INTO ae_analysis_category (analysis_category_id, analysis_id, category_id)
VALUES (11, 1, 1),
       (12, 1, 2),
       (21, 2, 1),
       (22, 2, 2),
       (41, 4, 1),
       (42, 4, 2)
;
INSERT INTO ae_analysis_feature (analysis_feature_id, analysis_id, agent_config_feature_id)
VALUES (11, 1, 1),
       (12, 1, 2),
       (13, 1, 3),
       (21, 2, 1),
       (22, 2, 2),
       (23, 2, 3),
       (31, 3, 1),
       (32, 3, 2),
       (33, 3, 3)
;
INSERT INTO ae_analysis_dataset (analysis_id, dataset_id)
VALUES (1, 1),
       (2, 2),
       (3, 2),
       (4, 2);

INSERT INTO ae_analysis_alert
VALUES (1, 1, now(), now()),
       (1, 2, now(), now()),
       (2, 3, now(), now()),
       (2, 4, now(), now()),
       (3, 3, now(), now()),
       (3, 4, now(), now()),
       (4, 3, now(), now()),
       (4, 4, now(), now())
;

-- Pending recommendations
INSERT INTO ae_pending_recommendation (analysis_id, alert_id, created_at)
VALUES (1, 1, now()),
       (1, 2, now()),
       (2, 3, now()),
       (2, 4, now()),
       (3, 3, now()),
       (3, 4, now()),
       (4, 3, now()),
       (4, 4, now())
;

-- COMMIT;

-- Category Values
INSERT INTO ae_match_category_value (match_id, category_id, created_at, value)
VALUES (11, 1, now(), 'DENY'),
       (11, 2, now(), 'C'),
       (21, 1, now(), 'PEPL'),
       (22, 1, now(), 'PEPL'),
       (22, 2, now(), 'I'),
       (31, 1, now(), 'DENY'),
       (31, 2, now(), 'C'),
       (32, 1, now(), 'DENY'),
       (32, 2, now(), 'C'),
       (33, 1, now(), 'DENY'),
       (33, 2, now(), 'C'),
       (34, 1, now(), 'DENY'),
       (34, 2, now(), 'C'),
       (35, 1, now(), 'DENY'),
       (35, 2, now(), 'C'),
       (36, 1, now(), 'DENY'),
       (36, 2, now(), 'C'),
       (37, 1, now(), 'DENY'),
       (37, 2, now(), 'C'),
       (38, 1, now(), 'DENY'),
       (38, 2, now(), 'C'),
       (41, 1, now(), 'DENY'),
       (41, 2, now(), 'C'),
       (42, 1, now(), 'DENY'),
       (42, 2, now(), 'C'),
       (43, 1, now(), 'DENY'),
       (43, 2, now(), 'C')
;

-- Feature Values
INSERT INTO ae_match_feature_value (match_id, agent_config_feature_id, created_at, value, reason)

-- @formatter:off
VALUES (11, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (11, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (11, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (21, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (21, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (21, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (22, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (22, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (31, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (31, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (31, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (32, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (32, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (32, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (33, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (33, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (33, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (34, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (34, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (34, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (35, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (35, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (35, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (36, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (36, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (36, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (37, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (37, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (37, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (38, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (38, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (38, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (41, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (41, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (41, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (42, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (42, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (42, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}'),
       (43, 1, now(), 'NID_NO_MATCH', '{"solution": "NID_NO_MATCH"}'),
       (43, 2, now(), 'DOC_MATCH', '{"solution": "DOC_MATCH"}'),
       (43, 3, now(), 'NAM_EXACT_MATCH', '{"solution": "NAM_EXACT_MATCH"}')
;
-- @formatter:on
