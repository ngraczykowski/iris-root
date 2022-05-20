SELECT apr.analysis_id,
       ac.category,
       aac.category_id,
       am.match_id,
       am.alert_id
FROM ae_pending_recommendation apr
         JOIN ae_match am ON am.alert_id = apr.alert_id
         JOIN ae_analysis_category aac ON apr.analysis_id = aac.analysis_id
         JOIN ae_category ac ON ac.category_id = aac.category_id
         LEFT JOIN ae_match_category_value amcv
                   ON amcv.match_id = am.match_id AND amcv.category_id = aac.category_id
WHERE amcv.category_id IS NULL
ORDER BY amcv.category_id, am.match_id;
