SELECT fv.match_id,
       fv.analysis_id,
       jsonb_object_agg(fv.feature, fv.context) AS context
FROM (SELECT amfv.match_id,
             aaa.analysis_id,
             aacf.feature,
             jsonb_build_object(
                 'solution', amfv.value,
                 'reason', amfv.reason,
                 'agentConfig', aacf.agent_config) AS context
      FROM ae_match_feature_value amfv
             JOIN ae_match am on am.match_id = amfv.match_id
             JOIN ae_analysis_alert aaa on aaa.alert_id = am.alert_id
             JOIN ae_agent_config_feature aacf
                  ON aacf.agent_config_feature_id = amfv.agent_config_feature_id
     ) fv
GROUP BY 1, 2
