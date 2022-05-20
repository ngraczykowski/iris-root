-- Delete all matches present in ae_match_feature_value
DELETE
FROM ae_agent_exchange_match_feature
WHERE array [agent_exchange_id, match_id] IN (
    SELECT array [aae.agent_exchange_id, aaemf.match_id]
    FROM ae_agent_exchange aae
             JOIN ae_agent_exchange_match_feature aaemf
                  on aae.agent_exchange_id = aaemf.agent_exchange_id
             JOIN (SELECT amfv.match_id,
                          amfv.agent_config_feature_id
                   FROM ae_match_feature_value amfv) existing
                  on aaemf.match_id = existing.match_id
                      AND aaemf.agent_config_feature_id = existing.agent_config_feature_id
);

-- Delete all exchanges without a single match
-- This cascades to ae_agent_exchange_feature
DELETE
FROM ae_agent_exchange
WHERE agent_exchange_id IN (
    SELECT aae.agent_exchange_id
    FROM ae_agent_exchange aae
             LEFT JOIN ae_agent_exchange_match_feature aaemf
                       on aae.agent_exchange_id = aaemf.agent_exchange_id
    WHERE aaemf.match_id IS NULL)
;
