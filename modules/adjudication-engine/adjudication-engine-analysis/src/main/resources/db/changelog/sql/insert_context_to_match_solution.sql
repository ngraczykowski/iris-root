UPDATE ae_match_solution
SET match_context = query.context
FROM (SELECT am.match_id,
             ams.analysis_id,
             JSONB_BUILD_OBJECT('matchId', am.client_match_identifier, 'solution', ams.solution,
                                'reason', ams.reason, 'categories',
                                COALESCE(accc.context, '{}'::jsonb), 'features',
                                COALESCE(acfc.context, '{}'::jsonb)) AS context
      FROM ae_match_solution ams
             JOIN ae_match am ON am.match_id = ams.match_id
             LEFT JOIN ae_comments_category_context accc ON am.match_id = accc.match_id
             LEFT JOIN ae_comments_feature_context acfc
                       ON am.match_id = acfc.match_id AND
                          acfc.analysis_id = ams.analysis_id) AS query
WHERE ae_match_solution.analysis_id = query.analysis_id
  AND ae_match_solution.match_id = query.match_id;
