-- ...and two alerts...
INSERT INTO ae_alert
VALUES (1, 'ads', NOW(), NOW(), 5);
INSERT INTO ae_alert
VALUES (2, 'ads', NOW(), NOW(), 5);

-- ...both pending in the analysis...
INSERT INTO ae_pending_recommendation
VALUES (1, 1, NOW()),
       (1, 2, NOW());

-- ...but one already has a recommendation...
INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action)
VALUES (1, 2, NOW(), 'TO_THE_MOON');

-- ...each has a single match...
INSERT INTO ae_match
VALUES (1, 1, NOW(), 'ads', 1);
INSERT INTO ae_match
VALUES (2, 1, NOW(), 'adss', 2);

-- ...which are solved...
INSERT INTO ae_match_solution (analysis_id, match_id, created_at, solution)
VALUES (1, 1, NOW(), 'SOLUTION_NO_DECISION'),
       (1, 2, NOW(), 'SOLUTION_NO_DECISION');

-- ...and all comment inputs are ready...
INSERT INTO ae_alert_comment_input (alert_id, created_at, value)
VALUES (1, NOW(), '{}'),
       (2, NOW(), '{}');
