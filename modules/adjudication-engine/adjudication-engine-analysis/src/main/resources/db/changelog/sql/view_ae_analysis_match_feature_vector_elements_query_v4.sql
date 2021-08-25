SELECT aaaq.analysis_id
     , am.alert_id
     , am.match_id
     , aamcaq.category_ids || ARRAY []::bigint[]             AS category_ids
     , aamcaq.category_values || ARRAY []::varchar[]         AS category_values
     , aamfaq.agent_config_feature_ids || ARRAY []::bigint[] AS agent_config_feature_ids
     , aamfaq.feature_values || ARRAY [] ::varchar[]         AS feature_values
FROM ae_analysis_alert aaaq
         JOIN ae_match am ON aaaq.alert_id = am.alert_id
         LEFT JOIN ae_analysis_match_categories_agg_query aamcaq
                   ON am.match_id = aamcaq.match_id
                       AND am.alert_id = aamcaq.alert_id
                       AND aamcaq.analysis_id = aaaq.analysis_id
         LEFT JOIN ae_analysis_match_features_agg_query aamfaq
                   ON am.match_id = aamfaq.match_id
                       AND am.alert_id = aamfaq.alert_id
                       AND aamfaq.analysis_id = aaaq.analysis_id
