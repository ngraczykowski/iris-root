--no filtered value and no decisions
INSERT INTO gns.fff_records (system_id) VALUES ('sysId-1');

--only filtered value
INSERT INTO gns.fff_records (system_id, filtered) VALUES ('sysId-2', '2019/09/20 08:48:58');

--filtered value and no reset decisions
INSERT INTO gns.fff_records (system_id, filtered) VALUES ('sysId-3', '2019/10/20 08:48:58');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (1, 'sysId-3', '2019/12/20 08:48:58', 0, 'DUMMY_USER');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (2, 'sysId-3', '2019/12/21 08:48:58', 12, 'DUMMY_USER');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (3, 'sysId-3', '2019/12/22 08:48:58', 1, 'FSK');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (4, 'sysId-3', '2019/12/23 08:48:58', 1, 'FFFFEED');

--filtered value and multiple reset decisions
INSERT INTO gns.fff_records (system_id, filtered) VALUES ('sysId-4', '2019/11/20 08:48:58');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (5, 'sysId-4', '2019/12/20 08:48:58', 0, 'FSK');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (6, 'sysId-4', '2019/12/21 08:48:58', 12, 'FSK');
INSERT INTO gns.fff_decisions (id, system_id, decision_date, type, operator)
VALUES (7, 'sysId-4', '2019/12/19 08:48:58', 12, 'FFFFEED');
