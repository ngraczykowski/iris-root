INSERT INTO ae_dataset (dataset_id, created_at)
VALUES (3, now());

INSERT INTO ae_dataset_alert (dataset_id, alert_id)
VALUES (3, 1);

INSERT INTO ae_analysis_dataset (analysis_id, dataset_id)
VALUES (1, 3);

INSERT INTO ae_analysis_alert
VALUES (1, 1, now(), now());

INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action, match_ids, match_contexts)
VALUES (1, 1, now(), 'TEST', array[]::integer[], '[]');
