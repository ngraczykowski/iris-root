-- Two analysis..
INSERT INTO ae_analysis
VALUES (1, 'policies/1', 'strategies/test', NOW()),
       (2, 'policies/1', 'strategies/test', NOW());

-- ...and three alerts...
INSERT INTO ae_alert
VALUES (1, 'blabla', NOW(), NOW(), 2),
       (2, 'blabla', NOW(), NOW(), 2),
       (3, 'blabla', NOW(), NOW(), 2);

-- ...two alerts pending in first analysis and are solved...
INSERT INTO ae_pending_recommendation
VALUES (1, 1, NOW());
INSERT INTO ae_pending_recommendation
VALUES (1, 2, NOW());

-- ...one pending in second analysis, but not solved...
INSERT INTO ae_pending_recommendation
VALUES (2, 1, NOW());

-- ...two solved for first analysis...
INSERT INTO ae_recommendation
VALUES (1, 1, 1, NOW(), 'blabla');
INSERT INTO ae_recommendation
VALUES (2, 1, 2, NOW(), 'babol');

-- ...third solved in second analysis and is not pending...
INSERT INTO ae_recommendation
VALUES (3, 2, 3, NOW(), 'blabla');
