WITH match_values AS (
    SELECT am.alert_id,
           am.match_id,
           ams.analysis_id,
           jsonb_build_object(
                   'matchId', am.client_match_identifier,
                   'solution', ams.solution,
                   'reason', ams.reason,
                   'categories', coalesce(cv.context, '{}'::JSONB),
                   'features', coalesce(fc.context, '{}'::JSONB)) AS context
    FROM ae_match am
             JOIN ae_match_solution ams ON am.match_id = ams.match_id
             LEFT JOIN ae_comments_category_context cv ON cv.match_id = am.match_id
             LEFT JOIN ae_comments_feature_context fc ON am.match_id = fc.match_id
)
SELECT mc.alert_id,
       mc.analysis_id,
       jsonb_agg(coalesce(mc.context, '{}'::JSONB)) AS match_context
FROM match_values mc
         LEFT JOIN ae_comments_feature_context fc ON mc.match_id = fc.match_id
         LEFT JOIN ae_comments_category_context cv ON cv.match_id = fc.match_id
GROUP BY 1, 2
