-- A single alert with a single match...
INSERT INTO ae_alert
VALUES (1000, 'alert', NOW(), NOW(), 5);
INSERT INTO ae_match
VALUES (1000, 1000, now(), 'match', 1);

-- ...in a single dataset...
INSERT INTO ae_dataset (dataset_id, created_at)
VALUES (1000, now());
INSERT INTO ae_dataset_alert (dataset_id, alert_id)
VALUES (1000, 1000);

-- ...with the comment input already fetched...
INSERT INTO ae_alert_comment_input (alert_id, created_at, value)
VALUES (1000, NOW(), '{}');

-- ...in two analysis: 1, and 2,...
INSERT INTO ae_analysis_dataset
VALUES (2, 1000)
     , (1, 1000)
;

-- ...with the match solved in both analysis...
INSERT INTO ae_match_solution (analysis_id, match_id, created_at, solution, reason, match_context)
VALUES (1, 1000, now(), 'SOLUTION_NO_DECISION', '{}', '{}')
     , (2, 1000, now(), 'SOLUTION_NO_DECISION', '{}', '{}')
;

-- ...with pending recommendation in second analysis...
INSERT INTO ae_pending_recommendation (analysis_id, alert_id, created_at)
VALUES (2, 1000, now());

-- ...and the recommendation in just the first analysis.
INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action,
                               match_ids, match_contexts)
VALUES (1, 1000, now(), 'GO_HOME_MATE', array[]::integer[], '[]');
