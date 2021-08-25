SELECT am.match_id,
       am.alert_id,
       array_agg(amfv.agent_config_feature_id
                 ORDER BY amfv.agent_config_feature_id)            as agent_config_feature_ids,
       array_agg(amfv.value ORDER BY amfv.agent_config_feature_id) as feature_values
FROM ae_match am
         JOIN ae_match_feature_value amfv on am.match_id = amfv.match_id
GROUP BY 1
;
