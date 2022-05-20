SELECT apr.alert_id,
       apr.analysis_id,
       array_agg(am.match_id ORDER BY am.sort_index)  AS match_ids,
       array_agg(ams.solution ORDER BY am.sort_index) AS match_solution
FROM ae_pending_recommendation apr
       JOIN ae_match am on am.alert_id = apr.alert_id
       JOIN ae_match_solution ams
            on am.match_id = ams.match_id AND apr.analysis_id = ams.analysis_id
WHERE ams.solution IS NOT NULL
GROUP BY 1, 2;
