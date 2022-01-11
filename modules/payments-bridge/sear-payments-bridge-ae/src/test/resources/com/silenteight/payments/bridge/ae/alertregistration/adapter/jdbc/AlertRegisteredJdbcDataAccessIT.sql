INSERT INTO pb_registered_alert (alert_name, fkco_system_id, registered_alert_id)
VALUES ('alerts/1', '1', 1);

INSERT INTO pb_registered_match (match_name, match_id, registered_alert_id)
VALUES ('alerts/1/matches/1', 'alerts/1/matches/1', 1);
INSERT INTO pb_registered_match (match_name, match_id, registered_alert_id)
VALUES ('alerts/1/matches/2', 'alerts/1/matches/2', 1);
