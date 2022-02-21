INSERT INTO ae_alert(alert_id, alerted_at, created_at, client_alert_identifier, priority)
VALUES (1, now(), now(), 'alert-1', 5),
       (2, now(), now(), 'alert-2', 5),
       (3, now(), now(), 'alert-3', 5);

-- Add comment input for alert
INSERT INTO ae_alert_comment_input (alert_id, created_at, value)
VALUES (1, now(), '{}');
