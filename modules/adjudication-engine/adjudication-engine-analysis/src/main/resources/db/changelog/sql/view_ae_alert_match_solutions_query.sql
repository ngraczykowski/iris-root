SELECT apr.alert_id,
       apr.analysis_id,
       ARRAY_AGG(am.match_id ORDER BY am.sort_index)  AS match_ids,
       ARRAY_AGG(ams.solution ORDER BY am.sort_index) AS match_solution
FROM ae_pending_recommendation apr
         JOIN ae_match am ON am.alert_id = apr.alert_id
         LEFT JOIN ae_match_solution ams ON am.match_id = ams.match_id
WHERE ams.solution IS NOT NULL
GROUP BY apr.alert_id, apr.analysis_id;
