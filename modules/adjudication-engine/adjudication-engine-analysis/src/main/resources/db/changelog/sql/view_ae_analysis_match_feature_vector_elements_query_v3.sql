WITH match_categories AS (
  SELECT aaaq.analysis_id,
         am.match_id,
         array_agg(amcv.category_id ORDER BY amcv.category_id) AS category_ids,
         array_agg(amcv.value ORDER BY amcv.category_id)       AS category_values
  FROM ae_analysis_alert aaaq
         JOIN ae_match am ON aaaq.alert_id = am.alert_id
         JOIN ae_match_category_value amcv ON am.match_id = amcv.match_id
         JOIN ae_analysis_category aac
              ON amcv.category_id = aac.category_id AND aaaq.analysis_id = aac.analysis_id
  GROUP BY 1, 2
),
     match_features AS (
       SELECT aaaq.analysis_id,
              am.match_id,
              array_agg(
                  amfv.agent_config_feature_id ORDER BY amfv.agent_config_feature_id
                ) AS agent_config_feature_ids,
              array_agg(
                  amfv.value ORDER BY amfv.agent_config_feature_id
                ) AS feature_values
       FROM ae_analysis_alert aaaq
              JOIN ae_match am ON aaaq.alert_id = am.alert_id
              JOIN ae_match_feature_value amfv ON am.match_id = amfv.match_id
              JOIN ae_analysis_feature aaf
                   ON amfv.agent_config_feature_id = aaf.agent_config_feature_id AND
                      aaaq.analysis_id = aaf.analysis_id
       GROUP BY 1, 2
     )
SELECT aaaq.analysis_id
     , am.alert_id
     , am.match_id
     , mc.category_ids || ARRAY []::bigint[]             AS category_ids
     , mc.category_values || ARRAY []::varchar[]         AS category_values
     , mf.agent_config_feature_ids || ARRAY []::bigint[] AS agent_config_feature_ids
     , mf.feature_values || ARRAY [] ::varchar[]         AS feature_values
FROM ae_analysis_alert aaaq
       JOIN ae_match am ON aaaq.alert_id = am.alert_id
       LEFT JOIN match_categories mc
                 ON am.match_id = mc.match_id AND mc.analysis_id = aaaq.analysis_id
       LEFT JOIN match_features mf
                 ON am.match_id = mf.match_id AND mf.analysis_id = aaaq.analysis_id
