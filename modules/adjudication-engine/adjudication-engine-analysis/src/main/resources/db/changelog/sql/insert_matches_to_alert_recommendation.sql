UPDATE ae_recommendation
SET match_contexts = query.matches,
    match_ids      = query.matches_ids
FROM ae_alert_recommendation_query AS query
WHERE ae_recommendation.analysis_id = query.analysis_id
  AND ae_recommendation.alert_id = query.alert_id;
