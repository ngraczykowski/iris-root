INSERT INTO ae_agent_config_feature (created_at, agent_config, feature)
VALUES (now(), 'agents/test/versions/9.99.999/configs/987', 'features/test-1'),
       (now(), 'agents/test/versions/9.99.999/configs/987', 'features/test-2'),
       (now(), 'agents/test/versions/9.99.999/configs/987', 'features/test-3')
;

INSERT INTO ae_agent_exchange (agent_exchange_id, created_at, request_id, request_priority,
                               agent_config)
VALUES (123, now(), '980e1f4c-6c5b-45d2-8516-0998776a39c8', 5,
        'agents/test/versions/9.99.999/configs/987')
;

INSERT INTO ae_agent_exchange_feature (agent_exchange_feature_id, agent_exchange_id, feature,
                                       created_at)
VALUES (456, 123, 'features/test-1', now()),
       (457, 123, 'features/test-2', now()),
       (458, 123, 'features/test-3', now())
;
