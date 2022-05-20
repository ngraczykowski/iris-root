SELECT apr.analysis_id,
       aa.alert_id,
       am.match_id,
       aaf.agent_config_feature_id,
       aacf.agent_config,
       aacf.feature,
       aa.priority
FROM ae_pending_recommendation apr
         JOIN ae_alert aa on apr.alert_id = aa.alert_id
         JOIN ae_match am on apr.alert_id = am.alert_id
         JOIN ae_analysis_feature aaf on apr.analysis_id = aaf.analysis_id
         JOIN ae_agent_config_feature aacf
              on aaf.agent_config_feature_id = aacf.agent_config_feature_id
         LEFT JOIN ae_match_feature_value amfv
                   on aaf.agent_config_feature_id = amfv.agent_config_feature_id and
                      am.match_id = amfv.match_id
         LEFT JOIN ae_pending_match_feature_query apmfq
                   on aaf.agent_config_feature_id = apmfq.agent_config_feature_id and
                      am.match_id = apmfq.match_id
WHERE amfv.match_id IS NULL
  AND apmfq.match_id IS NULL
ORDER BY aa.priority DESC, aacf.agent_config, am.match_id;
