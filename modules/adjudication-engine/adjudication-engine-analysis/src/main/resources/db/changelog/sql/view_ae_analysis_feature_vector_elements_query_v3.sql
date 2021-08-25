SELECT aa.analysis_id,
       aacaq.category_ids || ARRAY []::bigint[]             as category_ids,
       aafaq.agent_config_feature_ids || ARRAY []::bigint[] as agent_config_feature_ids,
       aacaq.category_names || ARRAY []::varchar[]          as category_names,
       aafaq.feature_names || ARRAY []::varchar[]           as feature_names
FROM ae_analysis aa
         LEFT JOIN ae_analysis_categories_agg_query aacaq on aa.analysis_id = aacaq.analysis_id
         LEFT JOIN ae_analysis_features_agg_query aafaq on aa.analysis_id = aafaq.analysis_id;
