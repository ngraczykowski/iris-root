INSERT INTO ae_dataset (dataset_id, created_at)
VALUES (1, now()),
       (2, now());

INSERT INTO ae_dataset_alert (dataset_id, alert_id)
SELECT 1, alert_id
FROM ae_alert
WHERE alert_id <= 5;

INSERT INTO ae_dataset_alert (dataset_id, alert_id)
SELECT 2, alert_id
FROM ae_alert
WHERE alert_id >= 3;
