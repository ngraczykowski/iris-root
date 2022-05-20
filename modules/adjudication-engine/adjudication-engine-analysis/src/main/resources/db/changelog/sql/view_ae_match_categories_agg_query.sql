SELECT am.match_id,
       am.alert_id,
       array_agg(amcv.category_id ORDER BY amcv.category_id) as category_ids,
       array_agg(amcv.value ORDER BY amcv.category_id)       as category_values
FROM ae_match am
         JOIN ae_match_category_value amcv on am.match_id = amcv.match_id
GROUP BY 1
;
