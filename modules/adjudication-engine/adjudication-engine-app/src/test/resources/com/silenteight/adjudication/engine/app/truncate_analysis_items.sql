DELETE FROM ae_agent_exchange_match_feature
WHERE agent_exchange_match_feature_id IN (100001, 100002, 100003);

DELETE FROM ae_agent_config_feature
WHERE agent_config_feature_id IN (1001);

DELETE FROM ae_agent_exchange_feature
WHERE agent_exchange_feature_id IN (100001, 100002, 100003);

DELETE FROM ae_agent_exchange
WHERE agent_exchange_id IN (1001, 1002, 1003);

DELETE FROM ae_pending_recommendation
WHERE analysis_id IN (1001);

DELETE FROM ae_match
WHERE match_id IN (100001, 100002, 100003);

DELETE FROM ae_alert
WHERE alert_id IN (10001, 10002, 10003);

DELETE FROM ae_analysis
WHERE analysis_id IN (1001);
