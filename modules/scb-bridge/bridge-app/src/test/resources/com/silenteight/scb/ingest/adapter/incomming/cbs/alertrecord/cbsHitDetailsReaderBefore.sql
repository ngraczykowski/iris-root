CREATE TABLE gns.cbs_hit_details_test
(
    system_id    VARCHAR(64)   NOT NULL,
    batch_id     varchar(64)   NOT NULL,
    hit_uniq_id  VARCHAR(1000) NOT NULL,
    hit_neo_flag VARCHAR(20),
    seq_no       VARCHAR(4000) NOT NULL,
    PRIMARY KEY (batch_id, system_id, hit_uniq_id)
);

CREATE VIEW gns.cbs_hit_details_test_view as
SELECT *
FROM gns.cbs_hit_details_test;

INSERT INTO gns.cbs_hit_details_test (system_id, batch_id, hit_uniq_id, hit_neo_flag, seq_no)
VALUES ('system_id_0', 'batch_id', 'uniq_id_1', null, '1'),
       ('system_id_1', 'batch_id', 'uniq_id_1', 'N', '1'),
       ('system_id_2', 'batch_id', 'uniq_id_1', 'E', '1'),
       ('system_id_3', 'batch_id', 'uniq_id_1', 'O', '1'),
       ('system_id_4', 'batch_id_1', 'uniq_id_1', 'N', '1'),
       ('system_id_4', 'batch_id_2', 'uniq_id_1', 'E', '1'),
       ('system_id_5', 'batch_id', 'uniq_id_1', 'N', '1'),
       ('system_id_5', 'batch_id', 'uniq_id_2', 'E', '2'),
       ('system_id_5', 'batch_id', 'uniq_id_3', 'O', '3');

COMMIT;
