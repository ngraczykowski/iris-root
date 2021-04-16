INSERT INTO ae_missing_features
    (SELECT aaf.feature, aaf.agent_config, am.match_id, aa.priority
     FROM ae_analysis_alert_query aaaq
              JOIN ae_analysis_features aaf on aaaq.analysis_id = aaf.analysis_id
              JOIN ae_feature af on aaf.feature = af.feature
              JOIN ae_agent_config aac on aaf.agent_config = aac.name
              JOIN ae_alert aa on aaaq.alert_id = aa.alert_id
              JOIN ae_match am on aa.alert_id = am.alert_id
              FULL OUTER JOIN ae_match_feature_value amfv
                  on am.match_id = amfv.match_id
                         and af.feature_id = amfv.feature_id
                         and aac.agent_config_id = amfv.agent_config_id
     WHERE amfv.match_id is null AND not exists(SELECT ));
