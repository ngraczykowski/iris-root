WITH match_categories AS (
    SELECT am.match_id,
           array_agg(amcv.category_id ORDER BY amcv.category_id) as category_ids
    FROM ae_match am
             JOIN ae_match_category_value amcv on am.match_id = amcv.match_id
    GROUP BY 1
),
     match_features AS (
         SELECT am.match_id,
                array_agg(amfv.agent_config_feature_id
                          ORDER BY amfv.agent_config_feature_id) as agent_config_feature_ids
         FROM ae_match am
                  JOIN ae_match_feature_value amfv on am.match_id = amfv.match_id
         GROUP BY 1
     )
SELECT am.alert_id,
       am.match_id,
       mc.category_ids || ARRAY []::bigint[]             as category_ids,
       mf.agent_config_feature_ids || ARRAY []::bigint[] as agent_config_feature_ids
FROM ae_match am
         LEFT JOIN match_categories mc on am.match_id = mc.match_id
         LEFT JOIN match_features mf on am.match_id = mf.match_id;
