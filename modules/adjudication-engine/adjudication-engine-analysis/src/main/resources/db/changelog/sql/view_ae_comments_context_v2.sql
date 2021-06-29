SELECT aa.alert_id,
       ar.analysis_id,
       ar.recommendation_id,
       ar.created_at,
       aa.client_alert_identifier,
       aaci.value         AS comment_input,
       ar.recommended_action,
       acmc.match_context AS matches,
       acmc.match_ids     AS matches_ids
FROM ae_recommendation ar
         JOIN ae_comments_match_context acmc
              ON ar.alert_id = acmc.alert_id AND ar.analysis_id = acmc.analysis_id
         JOIN ae_alert_comment_input aaci ON ar.alert_id = aaci.alert_id
         JOIN ae_alert aa ON aa.alert_id = ar.alert_id;
