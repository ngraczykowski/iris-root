DELETE FROM ae_alert_labels WHERE true;
DELETE FROM ae_match_feature_value WHERE true;
DELETE FROM ae_match_category_value WHERE true;
DELETE FROM ae_match_recommendation WHERE true;
DELETE FROM ae_match cascade WHERE true;
DELETE FROM ae_analysis_alert WHERE true;
DELETE FROM ae_alert cascade WHERE true;
DELETE FROM ae_analysis_category WHERE true;
DELETE FROM ae_analysis_feature WHERE true;
DELETE FROM ae_analysis WHERE true;
DELETE FROM ae_category WHERE true;
DELETE FROM ae_agent_config_feature WHERE true;


-- Feature config
INSERT INTO public.ae_agent_config_feature (agent_config_feature_id, created_at, agent_config,
                                            feature)
VALUES (1, '2022-05-06 10:14:20.422255', 'agents/name/versions/1.0.0/configs/1', 'features/name');
INSERT INTO public.ae_agent_config_feature (agent_config_feature_id, created_at, agent_config,
                                            feature)
VALUES (2, '2022-05-09 12:36:26.193143', 'agents/geo/versions/1.0.0/configs/1', 'features/geo');

-- Category config
INSERT INTO public.ae_category (category_id, created_at, category)
VALUES (1, '2022-05-31 12:13:30.689969', 'categories/test1');
INSERT INTO public.ae_category (category_id, created_at, category)
VALUES (2, '2022-05-31 12:13:30.689969', 'categories/test2');


-- Analysis config
INSERT INTO public.ae_analysis (analysis_id, policy, strategy, created_at, attach_metadata,
                                attach_recommendation)
VALUES (1, 'policy', 'startegy', '2022-05-06 08:14:20.440351', TRUE, TRUE);

INSERT INTO public.ae_analysis_feature (analysis_feature_id, analysis_id, agent_config_feature_id)
VALUES (1, 1, 1);

INSERT INTO public.ae_analysis_category (analysis_category_id, analysis_id, category_id)
VALUES (1, 1, 1);

-- Alert with two matches and values
INSERT INTO public.ae_alert (alert_id, client_alert_identifier, created_at, alerted_at, priority)
VALUES (1, '257', '2022-05-06 08:13:07.373764', '2022-05-06 08:13:07.339778', 5);

INSERT INTO public.ae_analysis_alert (analysis_id, alert_id, deadline_at, created_at)
VALUES (1, 1, NULL, '2022-05-06 10:14:20.524788');

INSERT INTO public.ae_match (match_id, alert_id, created_at, client_match_identifier, sort_index)
VALUES (1, 1, '2022-05-06 08:13:07.400305', '911', 0);

INSERT INTO public.ae_match (match_id, alert_id, created_at, client_match_identifier, sort_index)
VALUES (2, 1, '2022-05-06 08:13:07.400305', '912', 1);

INSERT INTO public.ae_match_category_value (match_id, category_id, created_at, value)
VALUES (1, 1, '2022-05-19 08:58:38.394104', 'NO_DECISION');
INSERT INTO public.ae_match_category_value (match_id, category_id, created_at, value)
VALUES (2, 1, '2022-05-19 08:58:38.394104', 'DECISION');

INSERT INTO public.ae_match_feature_value (match_id, agent_config_feature_id, created_at, value,
                                           reason)
VALUES (1, 1, '2022-05-06 10:14:20.814190', 'INCONCLUSIVE', '{}');
INSERT INTO public.ae_match_feature_value (match_id, agent_config_feature_id, created_at, value,
                                           reason)
VALUES (2, 1, '2022-05-06 10:14:20.814190', 'INCONCLUSIVE', '{}');

INSERT INTO public.ae_alert_labels (alert_id, name, value)
VALUES (1, 'matchQuantity', 'many');
