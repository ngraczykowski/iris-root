INSERT INTO pb_alert_message (alert_message_id, created_at, received_at, data_center, unit,
                              business_unit, message_id, system_id, priority, number_of_hits)
VALUES ('f07f327c-58c2-e2e5-b02d-b2bdeee79adc', now(), now(), 'dc', 'unit', 'businessunit',
        'messageId', 'systemId', 1, 1);

INSERT INTO pb_registered_alert
VALUES ('f07f327c-58c2-e2e5-b02d-b2bdeee79adc', 'alerts/1');

INSERT INTO pb_registered_match
VALUES ('f07f327c-58c2-e2e5-b02d-b2bdeee79adc', 'alerts/1/matches/1');
INSERT INTO pb_registered_match
VALUES ('f07f327c-58c2-e2e5-b02d-b2bdeee79adc', 'alerts/1/matches/2');
