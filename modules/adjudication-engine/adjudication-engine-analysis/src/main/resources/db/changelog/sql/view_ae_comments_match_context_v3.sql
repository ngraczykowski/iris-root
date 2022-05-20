SELECT mc.alert_id,
       mc.analysis_id,
       jsonb_agg(coalesce(mc.context, '{}'::JSONB)) AS match_context,
       array_agg(mc.match_id)                       AS match_ids
FROM (SELECT am.alert_id,
             am.match_id,
             ams.analysis_id,
             jsonb_build_object(
                     'matchId', am.client_match_identifier,
                     'solution', ams.solution,
                     'reason', ams.reason,
                     'categories', coalesce(accc.context, '{}'::JSONB),
                     'features', coalesce(acfc.context, '{}'::JSONB)) AS context
      FROM ae_match am
               JOIN ae_match_solution ams ON am.match_id = ams.match_id
               LEFT JOIN ae_comments_category_context accc ON am.match_id = accc.match_id
               LEFT JOIN ae_comments_feature_context acfc ON am.match_id = acfc.match_id
      ORDER BY am.sort_index) mc
GROUP BY 1, 2
