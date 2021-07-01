WITH match_categories AS (
    SELECT aaaq.analysis_id,
           am.match_id,
           array_agg(amcv.category_id ORDER BY amcv.category_id) as category_ids,
           array_agg(amcv.value ORDER BY amcv.category_id)       as category_values
    FROM ae_analysis_alert_query aaaq
             JOIN ae_match am on aaaq.alert_id = am.alert_id
             JOIN ae_match_category_value amcv on am.match_id = amcv.match_id
    WHERE amcv.category_id IN
          (SELECT category_id FROM ae_analysis_category WHERE analysis_id = aaaq.analysis_id)
    GROUP BY 1, 2
),
     match_features AS (
         SELECT aaaq.analysis_id,
                am.match_id,
                array_agg(
                        amfv.agent_config_feature_id ORDER BY amfv.agent_config_feature_id
                    ) as agent_config_feature_ids,
                array_agg(
                        amfv.value ORDER BY amfv.agent_config_feature_id
                    ) as feature_values
         FROM ae_analysis_alert_query aaaq
                  JOIN ae_match am on aaaq.alert_id = am.alert_id
                  JOIN ae_match_feature_value amfv on am.match_id = amfv.match_id
         WHERE amfv.agent_config_feature_id IN (
             SELECT agent_config_feature_id
             FROM ae_analysis_feature
             WHERE analysis_id = aaaq.analysis_id)
         GROUP BY 1, 2
     )
SELECT aaaq.analysis_id
     , am.alert_id
     , am.match_id
     , mc.category_ids || ARRAY []::bigint[]             as category_ids
     , mc.category_values || ARRAY []::varchar[]         as category_values
     , mf.agent_config_feature_ids || ARRAY []::bigint[] as agent_config_feature_ids
     , mf.feature_values || ARRAY [] ::varchar[]         as feature_values
FROM ae_analysis_alert_query aaaq
         JOIN ae_match am ON aaaq.alert_id = am.alert_id
         LEFT JOIN match_categories mc
                   ON am.match_id = mc.match_id AND mc.analysis_id = aaaq.analysis_id
         LEFT JOIN match_features mf
                   ON am.match_id = mf.match_id AND mf.analysis_id = aaaq.analysis_id
