SELECT q.feature, q.agent_config, q.priority
FROM ae_missing_feature_value_matches_query q
GROUP BY q.feature, q.agent_config, q.priority;
