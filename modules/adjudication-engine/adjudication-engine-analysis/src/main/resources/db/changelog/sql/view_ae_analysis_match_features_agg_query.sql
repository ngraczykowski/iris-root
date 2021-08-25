SELECT aaa.analysis_id,
       am.match_id,
       am.alert_id,
       array_agg(
               amfv.agent_config_feature_id ORDER BY amfv.agent_config_feature_id
           ) AS agent_config_feature_ids,
       array_agg(
               amfv.value ORDER BY amfv.agent_config_feature_id
           ) AS feature_values
FROM ae_analysis_alert aaa
         JOIN ae_match am ON aaa.alert_id = am.alert_id
         JOIN ae_match_feature_value amfv ON am.match_id = amfv.match_id
         JOIN ae_analysis_feature aaf
              ON amfv.agent_config_feature_id = aaf.agent_config_feature_id AND
                 aaa.analysis_id = aaf.analysis_id
GROUP BY 1, 2
