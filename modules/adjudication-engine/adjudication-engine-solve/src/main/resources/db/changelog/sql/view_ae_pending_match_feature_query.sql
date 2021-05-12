SELECT aae.agent_exchange_id,
       aacf.agent_config_feature_id,
       aaemf.match_id,
       aaemf.created_at
FROM ae_agent_exchange aae
         JOIN ae_agent_exchange_match_feature aaemf
              on aae.agent_exchange_id = aaemf.agent_exchange_id
         JOIN ae_agent_config_feature aacf
              on aae.agent_config = aacf.agent_config and aaemf.feature = aacf.feature;
