SELECT aaaq.alert_id, aaaq.analysis_id
FROM ae_analysis_alert_query aaaq
         LEFT JOIN ae_alert_comment_input aaci
                   ON aaci.alert_id = aaaq.alert_id
WHERE aaci.alert_id IS NULL;
