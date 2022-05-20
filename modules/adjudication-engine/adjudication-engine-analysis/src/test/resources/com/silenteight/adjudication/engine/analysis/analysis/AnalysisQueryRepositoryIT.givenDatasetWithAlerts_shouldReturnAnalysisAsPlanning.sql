-- Use dataset 2 comes from common fixtures.
INSERT INTO ae_analysis_dataset (analysis_id, dataset_id)
VALUES (1, 2);

INSERT INTO ae_analysis_alert
SELECT 1, alert_id, now(), now()
FROM ae_alert
WHERE alert_id >= 3
