INSERT INTO pb_registered_alert (registered_alert_id, alert_name, alert_message_id, fkco_system_id)
VALUES (1, 'alerts/1', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad1', 'systemId1'),
       (2, 'alerts/2', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad2', 'systemId2'),
       (3, 'alerts/3', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad3', 'systemId3'),
       (4, 'alerts/4', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad4', 'systemId4'),
       (5, 'alerts/5', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad5', 'systemId5'),
       (6, 'alerts/6', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad6', 'systemId6'),
       (7, 'alerts/7', 'f07f327c-58c2-e2e5-b02d-b2bdeee79ad7', 'systemId7'),
       (8, 'alerts/8','f07f327c-58c2-e2e5-b02d-b2bdeee79ad8', 'systemId8'),
       (9, 'alerts/9','f07f327c-58c2-e2e5-b02d-b2bdeee79ad9','systemId9'),
       (10, 'alerts/10','f07f327c-58c2-e2e5-b02d-b2bdeee79a10', 'systemId10');

INSERT INTO pb_registered_match (registered_alert_id, match_name)
VALUES (1, 'alerts/1/matches/1'),
       (1, 'alerts/1/matches/2'),
       (1, 'alerts/1/matches/3'),
       (2, 'alerts/2/matches/1'),
       (2, 'alerts/2/matches/2'),
       (3, 'alerts/3/matches/1'),
       (4, 'alerts/4/matches/1'),
       (5, 'alerts/5/matches/1'),
       (6, 'alerts/6/matches/1'),
       (7, 'alerts/7/matches/1'),
       (8, 'alerts/8/matches/1'),
       (9, 'alerts/9/matches/1'),
       (10, 'alerts/10/matches/1'),
       (10, 'alerts/10/matches/2');

