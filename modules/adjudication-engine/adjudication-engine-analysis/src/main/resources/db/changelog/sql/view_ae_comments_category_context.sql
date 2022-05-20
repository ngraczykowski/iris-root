SELECT amcv.match_id,
       jsonb_object_agg(ac.category, amcv.value) AS context
FROM ae_match_category_value amcv
         JOIN ae_category ac ON amcv.category_id = ac.category_id
GROUP BY 1
