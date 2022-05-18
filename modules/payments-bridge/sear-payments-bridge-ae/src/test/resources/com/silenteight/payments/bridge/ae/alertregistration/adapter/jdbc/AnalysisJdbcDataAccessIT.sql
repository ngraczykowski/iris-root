-- today's analysis
INSERT INTO pb_analysis
VALUES ('analysis/1', now() - INTERVAL '2 second');
INSERT INTO pb_analysis
VALUES ('analysis/2', now() - INTERVAL '1 second');

-- previous day analysis
INSERT INTO pb_analysis
VALUES ('analysis/3', '2021-09-21');
