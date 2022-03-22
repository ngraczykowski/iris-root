-- Two analysis
INSERT INTO ae_analysis (analysis_id, created_at, strategy, policy)
VALUES (1, now(), 'srategy', 'policy'),
       (2, now(), 'srategy', 'policy');

-- A single alert with a single match...
INSERT INTO ae_alert
VALUES (321, 'alert', now(), now(), 5);
INSERT INTO ae_match
VALUES (321, 321, now(), 'match', 1);

INSERT INTO ae_analysis_alert (analysis_id, alert_id, created_at)
VALUES (1, 321, now()),
       (2, 321, now());

-- ...with the match solved in both analysis...
INSERT INTO ae_match_solution (analysis_id, match_id, created_at, solution, reason, match_context)
VALUES (1, 321, now(), 'SOLUTION_NO_DECISION', '{}', '{}')
     , (2, 321, now(), 'SOLUTION_NO_DECISION', '{}', '{}')
;

-- ...and the recommendation in just the first analysis.
INSERT INTO ae_match_recommendation (analysis_id, match_id, created_at, recommended_action, comment)
VALUES (1, 321, now(), 'GO_HOME_MATE', 'some funny comment');
