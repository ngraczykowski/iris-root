SELECT aaf.feature, aaf.agent_config, am.match_id, MAX(aa.priority) as priority
FROM ae_analysis_alert_query aaaq
         JOIN ae_analysis_features aaf on aaaq.analysis_id = aaf.analysis_id
         JOIN ae_alert aa on aaaq.alert_id = aa.alert_id
         JOIN ae_match am on aa.alert_id = am.alert_id
         FULL OUTER JOIN ae_match_feature_value amfv on am.match_id = amfv.match_id
         FULL OUTER JOIN ae_agent_exchange_match_features aaemf on am.match_id = aaemf.match_id
WHERE amfv.match_id is null
  AND aaemf.match_id is null
GROUP BY aaf.feature, aaf.agent_config, am.match_id;
