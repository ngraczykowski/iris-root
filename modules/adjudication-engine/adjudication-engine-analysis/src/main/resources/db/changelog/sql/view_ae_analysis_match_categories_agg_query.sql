  SELECT aaa.analysis_id,
         am.match_id,
         am.alert_id,
         array_agg(amcv.category_id ORDER BY amcv.category_id) AS category_ids,
         array_agg(amcv.value ORDER BY amcv.category_id)       AS category_values
  FROM ae_analysis_alert aaa
         JOIN ae_match am ON aaa.alert_id = am.alert_id
         JOIN ae_match_category_value amcv ON am.match_id = amcv.match_id
         JOIN ae_analysis_category aac
              ON amcv.category_id = aac.category_id AND aaa.analysis_id = aac.analysis_id
  GROUP BY 1, 2
