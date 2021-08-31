INSERT INTO ae_agent_config_feature (agent_config_feature_id,
                                     created_at,
                                     agent_config,
                                     feature)
VALUES (1, now(), 'agents/name/versions/1.2.3/configs/1', 'features/name'),
       (2, now(), 'agents/date/versions/5.6.7/configs/2', 'features/dateOfBirth')
;

INSERT INTO ae_alert (alert_id, client_alert_identifier, created_at, alerted_at, priority)
VALUES (1, 'A1', now(), now(), 5),
       (2, 'A2', now(), now(), 8)
;

INSERT INTO ae_match (match_id, alert_id, created_at, client_match_identifier, sort_index)
VALUES (1, 1, now(), 'M1', 1),
       (2, 1, now(), 'M2', 2),
       (3, 2, now(), 'M3', 1),
       (4, 2, now(), 'M4', 2)
;
