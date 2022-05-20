WITH count AS (SELECT analysis_id, COUNT(alert_id) AS alert_count
               FROM ae_analysis_alert_query
               GROUP BY analysis_id),
     reco AS (SELECT analysis_id, COUNT(recommendation_id) AS recommended_alerts
              FROM ae_recommendation
              GROUP BY analysis_id)
SELECT aa.analysis_id,
       COALESCE(count.alert_count, 0)                                        AS alert_count,
       COALESCE(count.alert_count, 0) - COALESCE(reco.recommended_alerts, 0) AS pending_alerts
FROM ae_analysis aa
         LEFT JOIN count ON aa.analysis_id = count.analysis_id
         LEFT JOIN reco ON aa.analysis_id = reco.analysis_id;
