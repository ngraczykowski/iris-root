SELECT aa.alert_id,
       ar.analysis_id,
       ar.recommendation_id,
       ar.created_at,
       aa.client_alert_identifier,
       aaci.value AS comment_input,
       ar.recommended_action,
       ar.match_contexts,
       ar.match_ids
FROM ae_recommendation ar
         JOIN ae_alert_comment_input aaci ON ar.alert_id = aaci.alert_id
         JOIN ae_alert aa ON aa.alert_id = ar.alert_id;
