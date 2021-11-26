INSERT INTO ae_alert VALUES (1, 'id', now(), now(), 1);
INSERT INTO ae_alert VALUES (2, 'id', now(), now(), 1);

INSERT INTO ae_match VALUES (1, 1, now(), 'id', 1);

INSERT INTO ae_analysis VALUES (1, 'policy', 'strategy', now());
INSERT INTO ae_analysis_alert VALUES (1, 1, now(),now());

INSERT INTO ae_agent_config_feature VALUES (1, now(), 'config', 'feature');

INSERT INTO ae_match_feature_value VALUES (1, 1, now(), 'value', '{"value" : 1}');

INSERT INTO ae_alert_comment_input VALUES (1, 1, now(), '{"value": 1}');

INSERT INTO ae_recommendation VALUES (1, 1, 1, now(), 'action', ARRAY[1], '{"value": 1}');
