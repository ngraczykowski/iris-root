-- Categories
INSERT INTO ae_category (category_id, category, created_at)
VALUES (1, 'categories/country', now())
     , (2, 'categories/customer_type', now())
     , (3, 'categories/hit_category', now())
     , (4, 'categories/source_system', now())
     , (5, 'categories/hit_type', now())
     , (6, 'categories/segment', now())
;

-- Features
INSERT INTO ae_agent_config_feature (agent_config_feature_id, agent_config, feature, created_at)
VALUES (1, 'agents/date/versions/1.0.0/configs/1', 'features/dob', now())
     , (2, 'agents/name/versions/1.0.0/configs/1', 'features/name', now())
     , (3, 'agents/name/versions/1.2.1/configs/1', 'features/name', now())
     , (4, 'agents/name/versions/3.3.0/configs/1', 'features/name', now())
     , (5, 'agents/document/versions/2.1.0/configs/1', 'features/national_id', now())
     , (6, 'agents/document/versions/2.1.0/configs/1', 'features/passport', now())
;

-- Analysis
INSERT INTO ae_analysis (analysis_id, policy, strategy, created_at)
VALUES (1, 'policies/9da0a9eb-58ef-4a30-a8c9-c81750913495', 'strategies/BACK_TEST', now()),
       (2, 'policies/f07b39f2-46bd-47c9-92c0-160b77fe9537', 'strategies/USE_ANALYST_SOLUTION',
        now())
;

-- Analysis categories
INSERT INTO ae_analysis_category (analysis_id, category_id)
SELECT 1, *
FROM generate_series(1, 4);

INSERT INTO ae_analysis_category (analysis_id, category_id)
SELECT 2, *
FROM generate_series(3, 6);

-- Analysis features
INSERT INTO ae_analysis_feature (analysis_id, agent_config_feature_id)
SELECT 1, *
FROM generate_series(1, 4);

INSERT INTO ae_analysis_feature (analysis_id, agent_config_feature_id)
SELECT 2, *
FROM generate_series(3, 6);

-- Analysis labels
INSERT INTO ae_analysis_labels (analysis_id, name, value)
VALUES (1, 'source', 'TEST')
     , (2, 'source', 'TEST')
;
