SELECT am.alert_id,
       am.match_id,
       amcaq.category_ids || ARRAY []::bigint[]             as category_ids,
       amcaq.category_values || ARRAY []::varchar[]         as category_values,
       amfaq.agent_config_feature_ids || ARRAY []::bigint[] as agent_config_feature_ids,
       amfaq.feature_values || ARRAY []::varchar[]          as feature_values
FROM ae_match am
         LEFT JOIN ae_match_categories_agg_query amcaq
                   on am.match_id = amcaq.match_id AND am.alert_id = amcaq.alert_id
         LEFT JOIN ae_match_features_agg_query amfaq
                   on am.match_id = amfaq.match_id AND am.alert_id = amfaq.alert_id
;
