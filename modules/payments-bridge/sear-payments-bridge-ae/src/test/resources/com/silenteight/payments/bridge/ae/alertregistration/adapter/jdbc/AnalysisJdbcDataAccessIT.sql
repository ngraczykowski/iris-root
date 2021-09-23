-- today's analysis
INSERT INTO pb_analysis VALUES (1, now() - INTERVAL '2 second');
INSERT INTO pb_analysis VALUES (2, now() - INTERVAL '1 second');

-- previous day analysis
INSERT INTO pb_analysis VALUES (3, '2021-09-21');
