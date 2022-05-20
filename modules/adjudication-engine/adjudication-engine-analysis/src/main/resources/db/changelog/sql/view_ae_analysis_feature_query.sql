SELECT aaf.analysis_feature_id,
       aaf.analysis_id,
       aacf.agent_config_feature_id,
       aacf.agent_config,
       aacf.feature
FROM ae_analysis_feature aaf
         JOIN ae_agent_config_feature aacf
              on aaf.agent_config_feature_id = aacf.agent_config_feature_id;
