SELECT am.match_id, aac.category_id, aacf.agent_config_feature_id
FROM ae_pending_recommendation apr
         JOIN ae_match am on apr.alert_id = am.alert_id
         JOIN ae_analysis_category aac on apr.analysis_id = aac.analysis_id
         JOIN ae_analysis_feature aaf on aac.analysis_id = aaf.analysis_id
         JOIN ae_agent_config_feature aacf on aacf.agent_config_feature_id = aaf.agent_config_feature_id;
