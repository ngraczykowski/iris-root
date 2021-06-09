WITH analysis_categories AS (
    SELECT aa.analysis_id,
           array_agg(aac.category_id ORDER BY aac.category_id) as category_ids,
           array_agg(ac.category ORDER BY ac.category_id)      as category_names
    FROM ae_analysis aa
             JOIN ae_analysis_category aac on aa.analysis_id = aac.analysis_id
             JOIN ae_category ac on aac.category_id = ac.category_id
    GROUP BY 1
),
     analysis_features AS (
         SELECT aa.analysis_id,
                array_agg(
                        aaf.agent_config_feature_id
                        ORDER BY aaf.agent_config_feature_id
                    ) as agent_config_feature_ids,
                array_agg(
                        aacf.feature
                        ORDER BY aacf.agent_config_feature_id
                    ) as feature_names
         FROM ae_analysis aa
                  JOIN ae_analysis_feature aaf on aa.analysis_id = aaf.analysis_id
                  JOIN ae_agent_config_feature aacf
                       on aaf.agent_config_feature_id = aacf.agent_config_feature_id
         GROUP BY 1
     )
SELECT aa.analysis_id,
       ac.category_ids || ARRAY []::bigint[]             as category_ids,
       af.agent_config_feature_ids || ARRAY []::bigint[] as agent_config_feature_ids,
       ac.category_names || ARRAY []::varchar[]             as category_names,
       af.feature_names || ARRAY []::varchar[] as feature_names
FROM ae_analysis aa
         LEFT JOIN analysis_categories ac on aa.analysis_id = ac.analysis_id
         LEFT JOIN analysis_features af on aa.analysis_id = af.analysis_id;
