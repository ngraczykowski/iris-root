SELECT aa.analysis_id,
       array_agg(aaf.agent_config_feature_id
                 ORDER BY aaf.agent_config_feature_id)               as agent_config_feature_ids,
       array_agg(aacf.feature ORDER BY aacf.agent_config_feature_id) as feature_names
FROM ae_analysis aa
         JOIN ae_analysis_feature aaf on aa.analysis_id = aaf.analysis_id
         JOIN ae_agent_config_feature aacf
              on aaf.agent_config_feature_id = aacf.agent_config_feature_id
GROUP BY 1
