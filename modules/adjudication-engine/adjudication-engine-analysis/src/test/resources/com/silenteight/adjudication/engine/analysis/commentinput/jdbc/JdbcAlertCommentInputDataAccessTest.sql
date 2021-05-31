INSERT INTO ae_alert(alert_id, alerted_at, created_at, client_alert_identifier, priority)
VALUES (1, NOW(), NOW(), 'some-uuid-1', 5);

INSERT INTO ae_analysis(analysis_id, policy, strategy, created_at, state)
VALUES (1, 'policies/1', 'strategies/1', NOW(), 'NEW');

INSERT INTO ae_dataset(dataset_id, created_at)
VALUES (1, NOW());

INSERT INTO ae_dataset_alert(dataset_id, alert_id)
VALUES (1, 1);

INSERT INTO ae_analysis_dataset(analysis_id, dataset_id)
VALUES (1, 1);

