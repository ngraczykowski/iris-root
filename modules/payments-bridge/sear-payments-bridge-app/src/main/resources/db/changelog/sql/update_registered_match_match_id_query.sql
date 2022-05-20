WITH hit_payload AS (SELECT pra.alert_message_id,
                            jsonb_array_elements(pamp.original_message -> 'Hits') AS hit
                     FROM pb_registered_alert pra
                            JOIN pb_alert_message_payload pamp
                                 ON pra.alert_message_id = pamp.alert_message_id),
     hit AS (SELECT alert_message_id,
                    hit -> 'Hit' -> 'HittedEntity' -> 'ID' ->> 0 AS id,
                    hit -> 'Hit' -> 'Tag' ->> 0                  AS tag
             FROM hit_payload),
     alert_hit AS (SELECT *, row_number() OVER (PARTITION BY alert_message_id) AS sequence
                   FROM hit),
     match AS (SELECT prm.alert_message_id,
                      prm.match_name,
                      concat(ah.id, '(', ah.tag, ', #', ah.sequence, ')') AS match_id,
                      ah.sequence                                         AS hit_sequence
               FROM alert_hit ah
                      JOIN pb_registered_match prm ON ah.alert_message_id = prm.alert_message_id),
     match_registered AS (SELECT alert_message_id,
                                 match_name,
                                 array_agg(match_id)                                         AS match_id,
                                 row_number()
                                 OVER (PARTITION BY array_agg(match_id) ORDER BY match_name) AS hit_sequence
                          FROM match
                          GROUP BY alert_message_id, match_name
                          ORDER BY match_id, match_name)
UPDATE pb_registered_match prm
SET match_id = match_r.match_id
FROM (SELECT mr.match_name, m.match_id
      FROM match_registered mr
             JOIN match m ON mr.match_name = m.match_name
      WHERE mr.hit_sequence = m.hit_sequence) match_r
WHERE match_r.match_name = prm.match_name
