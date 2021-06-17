INSERT INTO ae_dataset (dataset_id, created_at)
VALUES (3, now());

INSERT INTO ae_dataset_alert (dataset_id, alert_id)
VALUES (3, 1);

-- We add two datasets: 1 and 3, and add a recommendation to alert 1, so there shall be more
-- pending alerts, yet at least one solved.
INSERT INTO ae_analysis_dataset (analysis_id, dataset_id)
VALUES (1, 3)
     , (1, 1)
;

INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action)
VALUES (1, 1, now(), 'TEST');
