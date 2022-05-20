INSERT INTO ae_alert(alert_id, alerted_at, created_at, client_alert_identifier, priority)
VALUES (1, now(), now(), 'some-uuid-1', 5);

INSERT INTO ae_match(match_id, alert_id, created_at, client_match_identifier, sort_index)
VALUES (1, 1, now(), 'some-uuid-1', 1);

INSERT INTO ae_category(category, created_at)
VALUES ('categories/sourceSystem', now());
INSERT INTO ae_category(category, created_at)
VALUES ('categories/customerType', now());
INSERT INTO ae_category(category, created_at)
VALUES ('categories/hitType', now());
INSERT INTO ae_category(category, created_at)
VALUES ('categories/country', now());

INSERT INTO ae_analysis
VALUES (1, '', '', now());

INSERT INTO ae_analysis_category
VALUES (0, 1, 1);

INSERT INTO ae_pending_recommendation
VALUES (1, 1, now());
