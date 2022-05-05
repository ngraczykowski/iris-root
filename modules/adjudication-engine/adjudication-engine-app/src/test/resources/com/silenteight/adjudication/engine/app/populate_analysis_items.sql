-- add analyze to ae_analysis
INSERT INTO ae_analysis
VALUES (1001, 'policies/1', 'strategies/test', NOW());

-- add alerts to ae_alert
INSERT INTO ae_alert
VALUES (10001, 'blabla', NOW(), NOW(), 2),
       (10002, 'blabla', NOW(), NOW(), 2),
       (10003, 'blabla', NOW(), NOW(), 2);

-- add add matches to ae_match
INSERT INTO ae_match
VALUES (100001, 10001, NOW(), 'blablabla', 1),
       (100002, 10002, NOW(), 'blablabla', 1),
       (100003, 10003, NOW(), 'blablabla', 1);

-- add relation between alerts and analysis in ae_pending_recommendation
INSERT INTO ae_pending_recommendation
VALUES (1001, 10001, NOW()),
       (1001, 10002, NOW()),
       (1001, 10003, NOW());

-- add items to ae_agent_exchange
INSERT INTO ae_agent_exchange
VALUES (1001, NOW(), '7e465bc0-b661-11ec-b909-0242ac120002', 2, 'agent_config'),
       (1002, NOW(), '9d2b4866-b661-11ec-b909-0242ac120002', 2, 'agent_config'),
       (1003, NOW(), 'a73e486c-b661-11ec-b909-0242ac120002', 2, 'agent_config');

-- add items to ae_agent_exchange_feature
INSERT INTO ae_agent_exchange_feature
VALUES (100001, 1001, 'features/name', NOW()),
       (100002, 1002, 'features/name', NOW()),
       (100003, 1003, 'features/name', NOW());

-- add items to ae_agent_config_feature
INSERT INTO ae_agent_config_feature
VALUES (1001, NOW(), '', 'features/name');

-- add items to ae_agent_exchange_match_feature
INSERT INTO ae_agent_exchange_match_feature
VALUES (100001, 1001, 1001, 100001, NOW()),
       (100002, 1002, 1001, 100002, NOW()),
       (100003, 1003, 1001, 100003, NOW());
