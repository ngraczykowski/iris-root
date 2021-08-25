-- Delete all matches present in ae_match_feature_value
DELETE
FROM ae_agent_exchange_match
WHERE array [agent_exchange_id, match_id] IN (
    SELECT array [aae.agent_exchange_id, aaem.match_id]
    FROM ae_agent_exchange aae
             JOIN ae_agent_exchange_match aaem
                  on aae.agent_exchange_id = aaem.agent_exchange_id
             JOIN ae_agent_exchange_feature aaef
                  on aae.agent_exchange_id = aaef.agent_exchange_id
             JOIN (SELECT amfv.match_id,
                          array [aacf.agent_config, aacf.feature] as agent_config_feature
                   FROM ae_match_feature_value amfv
                            JOIN ae_agent_config_feature aacf
                                 on amfv.agent_config_feature_id = aacf.agent_config_feature_id) existing
                  on aaem.match_id = existing.match_id
                      AND array [aae.agent_config, aaef.feature] = existing.agent_config_feature
);

-- Delete all exchanges without a single match
-- This cascades to ae_agent_exchange_feature
DELETE
FROM ae_agent_exchange
WHERE agent_exchange_id IN (
    SELECT aae.agent_exchange_id
    FROM ae_agent_exchange aae
             LEFT JOIN ae_agent_exchange_match aaem
                       on aae.agent_exchange_id = aaem.agent_exchange_id
    WHERE aaem.match_id IS NULL)
;
