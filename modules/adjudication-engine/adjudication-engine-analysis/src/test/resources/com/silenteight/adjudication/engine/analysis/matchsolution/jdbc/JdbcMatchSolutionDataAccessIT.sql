-- Two analysis...
INSERT INTO ae_analysis
VALUES (1, 'bla', 'bla', NOW()),
       (2, 'bla', 'bla', NOW());

-- ...and two alerts...
INSERT INTO ae_alert
VALUES (1, 'ads', NOW(), NOW(), 5),
       (2, 'ads', NOW(), NOW(), 5);

-- ...Two matches..
INSERT INTO ae_match
VALUES (1, 1, NOW(), 'asd', 1),
       (2, 2, NOW(), 'asd', 2);

-- ..One solution..
INSERT INTO ae_match_solution (match_solution_id, analysis_id, match_id, created_at, solution)
VALUES (1, 1, 1, NOW(), 'asd')
