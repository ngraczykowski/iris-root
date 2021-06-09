INSERT INTO public.ae_analysis (analysis_id, policy, strategy, created_at, state)
VALUES (1, 'policies/1', 'strategies/str', '2021-06-02 10:06:29.000000', 'NEW');
INSERT INTO public.ae_analysis_category (analysis_category_id, analysis_id, category_id)
VALUES (1, 1, 1);
INSERT INTO public.ae_analysis_category (analysis_category_id, analysis_id, category_id)
VALUES (2, 1, 2);
INSERT INTO public.ae_category (category_id, created_at, category)
VALUES (1, '2021-06-02 11:44:24.000000', 'categories/country');
INSERT INTO public.ae_category (category_id, created_at, category)
VALUES (2, '2021-06-02 11:44:24.000000', 'categories/ser');
INSERT INTO public.ae_analysis_feature (analysis_feature_id, analysis_id, agent_config_feature_id)
VALUES (1, 1, 1);
INSERT INTO public.ae_agent_config_feature
VALUES (1, '2021-05-14 07:25:52.000000', 'agents/document/versions/2.1.0/configs/1',
        'features/passport');

