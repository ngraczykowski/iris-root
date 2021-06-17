INSERT INTO ae_alert(alert_id, alerted_at, created_at, client_alert_identifier, priority)
VALUES (1, now(), now(), 'alert-1', 5),
       (2, now(), now(), 'alert-2', 5),
       (3, now(), now(), 'alert-3', 5);

INSERT INTO ae_analysis(analysis_id, policy, strategy, created_at)
VALUES (1, 'policies/1', 'strategies/1', now());

INSERT INTO ae_dataset(dataset_id, created_at)
VALUES (1, now()),
       (2, now());

-- Put all alerts into the dataset 1.
INSERT INTO ae_dataset_alert(dataset_id, alert_id)
SELECT 1, alert_id
FROM ae_alert;

-- Then single alert into the dataset 2
INSERT INTO ae_dataset_alert(dataset_id, alert_id)
VALUES (2, 1);

-- Add all datasets to analysis
INSERT INTO ae_analysis_dataset(analysis_id, dataset_id)
VALUES (1, 1),
       (1, 2);
