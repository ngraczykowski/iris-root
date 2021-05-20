SELECT aae.agent_exchange_id,
       aacf.agent_config_feature_id,
       aaem.match_id,
       aaem.created_at
FROM ae_agent_exchange aae
         JOIN ae_agent_exchange_match aaem
              on aae.agent_exchange_id = aaem.agent_exchange_id
         JOIN ae_agent_exchange_feature aaef
              on aae.agent_exchange_id = aaef.agent_exchange_id
         JOIN ae_agent_config_feature aacf
              on aae.agent_config = aacf.agent_config and
                 aaef.feature = aacf.feature;
