SELECT fv.match_id, jsonb_object_agg(fv.feature, fv.context) as context
FROM (SELECT amfv.match_id,
             aacf.feature,
             jsonb_build_object(
                     'solution', amfv.value,
                     'reason', amfv.reason,
                     'agentConfig', aacf.agent_config
                 ) as context
      FROM ae_match_feature_value amfv
               JOIN ae_agent_config_feature aacf
                    on aacf.agent_config_feature_id = amfv.agent_config_feature_id) fv
GROUP BY 1
