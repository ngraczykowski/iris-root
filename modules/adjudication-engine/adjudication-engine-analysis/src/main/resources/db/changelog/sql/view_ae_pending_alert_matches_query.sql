SELECT am.alert_id,
       apr.analysis_id,
       array_agg(am.match_id ORDER BY am.sort_index) AS match_ids
FROM ae_pending_recommendation apr
       JOIN ae_match am USING (alert_id)
GROUP BY 1, 2
