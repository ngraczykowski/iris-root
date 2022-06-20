INSERT INTO gns.fff_records (system_id, batch_id) VALUES ('system-id-1', 'batch-id-1');
INSERT INTO gns.fff_records (system_id, batch_id) VALUES ('system-id-2', 'batch-id-2');
INSERT INTO gns.fff_records (system_id, batch_id) VALUES ('system-id-3', 'batch-id-3');
INSERT INTO gns.fff_records (system_id, batch_id) VALUES ('system-id-4', null);

INSERT INTO gns.fff_hits_details (system_id) VALUES ('system-id-1');
INSERT INTO gns.fff_hits_details (system_id) VALUES ('system-id-2');
INSERT INTO gns.fff_hits_details (system_id) VALUES ('system-id-3');
INSERT INTO gns.fff_hits_details (system_id) VALUES ('system-id-4');

INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
  VALUES (1, 'system-id-1', '2019/12/20 08:48:58', 0, 'DUMMY_USER');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
  VALUES (2, 'system-id-1', '2019/12/22 08:48:58', 1, 'DUMMY_USER');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
  VALUES (3, 'system-id-2', '2019/12/20 08:48:58', 0, 'DUMMY_USER');
