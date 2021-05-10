INSERT INTO ae_pending_recommendation
SELECT aaaq.analysis_id, aaaq.alert_id, now()
FROM ae_analysis_alert_query aaaq
         LEFT JOIN ae_recommendation ar
                   on aaaq.alert_id = ar.alert_id and aaaq.analysis_id = ar.analysis_id
         LEFT JOIN ae_pending_recommendation apr
                   on aaaq.alert_id = apr.alert_id and aaaq.analysis_id = ar.analysis_id
WHERE aaaq.analysis_id = ?
  AND ar.recommendation_id IS NULL
  AND apr.created_at IS NULL
ON CONFLICT DO NOTHING
