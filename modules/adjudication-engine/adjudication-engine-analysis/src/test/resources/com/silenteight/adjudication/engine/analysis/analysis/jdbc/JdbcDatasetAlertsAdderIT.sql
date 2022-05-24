INSERT INTO ae_dataset
VALUES (1, now()),
       (2, now());

INSERT INTO ae_alert
VALUES (1, 'asd', now(), now(), 5),
       (2, 'asd', now(), now(), 5),
       (3, 'asd', now(), now(), 5);

INSERT INTO ae_dataset_alert
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3);

INSERT INTO ae_analysis
VALUES (1, '', '', now());

INSERT INTO ae_analysis_alert
VALUES (1, 1, now(), now());
