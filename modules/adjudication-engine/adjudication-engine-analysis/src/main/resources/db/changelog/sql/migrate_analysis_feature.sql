INSERT INTO ae_agent_config_feature(agent_config, feature, created_at)
    (SELECT DISTINCT agent_config, feature, now()
     FROM ae_analysis_features_migration
     ORDER BY agent_config);

INSERT INTO ae_analysis_feature(analysis_id, agent_config_feature_id)
    (SELECT aafm.analysis_id, aacf.agent_config_feature_id
     FROM ae_analysis_features_migration aafm
              JOIN ae_agent_config_feature aacf
                   on aafm.agent_config = aacf.agent_config
                       and aafm.feature = aacf.feature);
