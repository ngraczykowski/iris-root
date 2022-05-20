WITH count AS (SELECT analysis_id, count(alert_id) AS alert_count
               FROM ae_analysis_alert
               GROUP BY analysis_id),
     reco AS (SELECT analysis_id, count(recommendation_id) AS recommended_alerts
              FROM ae_recommendation
              GROUP BY analysis_id),
     dataset AS (SELECT analysis_id, count(dataset_id) AS dataset_count
                 FROM ae_analysis_dataset
                 GROUP BY analysis_id)
SELECT aa.analysis_id,
       coalesce(count.alert_count, 0)                                        AS alert_count,
       coalesce(count.alert_count, 0) - coalesce(reco.recommended_alerts, 0) AS pending_alerts,
       coalesce(dataset.dataset_count, 0)                                    AS dataset_count
FROM ae_analysis aa
       LEFT JOIN count ON aa.analysis_id = count.analysis_id
       LEFT JOIN reco ON aa.analysis_id = reco.analysis_id
       LEFT JOIN dataset ON aa.analysis_id = dataset.analysis_id
;
