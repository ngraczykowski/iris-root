-- Create alerts with matches
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (1,now(),now(),'some-uuid-1',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (2,now(),now(),'some-uuid-2',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (3,now(),now(),'some-uuid-3',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (4,now(),now(),'some-uuid-4',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (5,now(),now(),'some-uuid-5',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (6,now(),now(),'some-uuid-6',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (7,now(),now(),'some-uuid-7',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (8,now(),now(),'some-uuid-8',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (9,now(),now(),'some-uuid-9',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (10,now(),now(),'some-uuid-10',5);
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (12,'2007-12-03',now(),'some-uuid-11',5);

-- Alert outside time range
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (11,'2006-01-01',now(),'some-uuid-11',5);

-- Insert matches into all alerts
INSERT INTO ae_match (SELECT aa.alert_id, aa.alert_id, now(), 'clientId', 1 FROM ae_alert aa);

-- Create alert without match
INSERT INTO ae_alert(alert_id,alerted_at,created_at,client_alert_identifier,priority) VALUES (14,now(),now(),'some-uuid-13',5);

-- Add labels to alerts
INSERT INTO ae_alert_labels VALUES (1, 'label', 'value');
INSERT INTO ae_alert_labels VALUES (2, 'label', 'value');
