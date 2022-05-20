SELECT aaef.agent_exchange_feature_id,
       aae.request_id,
       aae.agent_config,
       aaef.feature,
       aacf.agent_config_feature_id
FROM ae_agent_exchange aae
         JOIN ae_agent_exchange_feature aaef
              on aae.agent_exchange_id = aaef.agent_exchange_id
         JOIN ae_agent_config_feature aacf
              on aae.agent_config = aacf.agent_config and
                 aaef.feature = aacf.feature
ORDER BY aae.agent_exchange_id, aaef.feature;
