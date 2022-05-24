INSERT INTO ae_alert VALUES (1, 'id', now(), now(), 1);
INSERT INTO ae_alert VALUES (2, 'id', now(), now(), 1);
INSERT INTO ae_alert VALUES (3, 'id', now(), now(), 1);
INSERT INTO ae_alert VALUES (4, 'id', now(), now(), 1);
INSERT INTO ae_alert VALUES (5, 'id', now(), now(), 1);

-- Create multiple matches
INSERT INTO ae_match VALUES (1, 1, now(), 'id1', 1);
INSERT INTO ae_match VALUES (2, 1, now(), 'id2', 2);
INSERT INTO ae_match VALUES (3, 2, now(), 'id3', 3);
INSERT INTO ae_match VALUES (4, 2, now(), 'id4', 4);
INSERT INTO ae_match VALUES (5, 3, now(), 'id5', 5);
INSERT INTO ae_match VALUES (6, 3, now(), 'id6', 6);

-- Create single matches
INSERT INTO ae_match VALUES (8, 4, now(), 'id7', 7);
INSERT INTO ae_match VALUES (9, 5, now(), 'id8', 8);

-- Add labels to alerts
INSERT INTO ae_alert_labels VALUES (4, 'solving', 'CMAPI');
INSERT INTO ae_alert_labels VALUES (5, 'solving', 'CMAPI');

INSERT INTO ae_dataset VALUES (1, now());
INSERT INTO ae_dataset VALUES (2, now());
INSERT INTO ae_dataset VALUES (3, now());

INSERT INTO ae_dataset_alert VALUES (1, 1);
INSERT INTO ae_dataset_alert VALUES (1, 2);

INSERT INTO ae_dataset_alert VALUES (2, 3);
