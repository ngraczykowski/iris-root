SELECT mc.alert_id,
       mc.analysis_id,
       JSONB_AGG(mc.match_context) AS match_context,
       ARRAY_AGG(mc.match_id)      AS match_ids
FROM (SELECT am.alert_id,
             am.match_id,
             ams.analysis_id,
             ams.match_context
      FROM ae_match am
             JOIN ae_match_solution ams ON am.match_id = ams.match_id
      ORDER BY am.sort_index) mc
GROUP BY 1, 2
