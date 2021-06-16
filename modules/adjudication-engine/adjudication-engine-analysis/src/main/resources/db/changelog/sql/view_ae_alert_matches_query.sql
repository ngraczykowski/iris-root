WITH match_ids AS (
    SELECT am.alert_id,
           array_agg(am.match_id ORDER BY am.sort_index) as match_ids
    FROM ae_match am
    GROUP BY am.alert_id
)
SELECT aa.alert_id,
       mi.match_ids || ARRAY []::bigint[] as match_ids
FROM ae_alert aa
         LEFT JOIN match_ids mi ON mi.alert_id = aa.alert_id
