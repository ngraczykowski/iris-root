SELECT aa.alert_id,
       ar.analysis_id,
       ar.recommendation_id,
       ar.created_at,
       aa.client_alert_identifier,
       ar.recommended_action,
       ar.match_contexts,
       ar.match_ids,
       ar.comment,
       ar.match_comments
FROM ae_recommendation ar
         JOIN ae_alert aa ON aa.alert_id = ar.alert_id;
