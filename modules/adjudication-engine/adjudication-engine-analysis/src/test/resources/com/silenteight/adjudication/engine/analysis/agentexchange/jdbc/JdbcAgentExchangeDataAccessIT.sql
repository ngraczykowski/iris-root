INSERT INTO ae_agent_exchange VALUES (1, now(), '980e1f4c-6c5b-45d2-8516-0998776a39c8', 5,
                                      'agents/test/versions/9.99.999/configs/987');

INSERT INTO ae_agent_config_feature VALUES (1, now(), 'agents/date/versions/1.0.0/configs/1', 'features/dob');
INSERT INTO ae_agent_config_feature VALUES (2, now(), 'agents/name/versions/1.0.0/configs/1', 'features/name');

INSERT INTO ae_agent_exchange_feature VALUES (1, 1, 'features/dob', now());

INSERT INTO ae_alert VALUES (1, '980e1f4c-6c5b-45d2-8516-0998776a39c8',  now(), now(), 6);

INSERT INTO ae_match VALUES (1, 1, now(), 'DB00051992', 2);
INSERT INTO ae_match VALUES (2, 1, now(), 'DB00051993', 3);

INSERT INTO ae_agent_exchange_match_feature VALUES (1, 1, 1, 1, now());
INSERT INTO ae_agent_exchange_match_feature VALUES (2, 1, 2, 1, now());

-- Independent values that should not be deleted

INSERT INTO ae_agent_exchange VALUES (2, now(), '980e1f4c-6c5b-45d2-8516-0998776a39c7', 5,
                                      'agents/test/versions/9.99.999/configs/917');

INSERT INTO ae_agent_config_feature VALUES (3, now(), 'agents/date/versions/1.0.0/configs/2', 'features/dob');
INSERT INTO ae_agent_config_feature VALUES (4, now(), 'agents/name/versions/1.0.0/configs/2', 'features/name');

INSERT INTO ae_agent_exchange_feature VALUES (2, 2, 'features/dob', now());

INSERT INTO ae_alert VALUES (2, '980e1f4c-6c5b-45d2-8516-0998776a39c9',  now(), now(), 6);

INSERT INTO ae_match VALUES (3, 2, now(), 'DB00051995', 2);
INSERT INTO ae_match VALUES (4, 2, now(), 'DB00051996', 3);

INSERT INTO ae_agent_exchange_match_feature VALUES (3, 2, 2, 3, now());
INSERT INTO ae_agent_exchange_match_feature VALUES (4, 2, 3, 4, now());

