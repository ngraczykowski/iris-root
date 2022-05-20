INSERT INTO uds_comment_input(alert_name, alert_comment_input, match_comment_inputs)
VALUES ('alerts/1', '{"field1"  : ""}', '[{"match":"alerts/1/matches/1","commentInput":{"field1":"MatchComment number 1"}},{"match":"alerts/1/matches/2","commentInput":{"field1":"MatchComment number 1"}}]'),
       ('alerts/2', '{"field1"  : ""}', '[{"match":"alerts/2/matches/3","commentInput":{"field1":"MatchComment number 1"}},{"match":"alerts/1/matches/4","commentInput":{"field1":"MatchComment number 1"}}]'),
       ('alerts/3', '{}', '{}'),
       ('alerts/4', '{}', '{}'),
       ('alerts/5', '{}', '{}'),
       ('alerts/6', '{}', '{}'),
       ('alerts/7', '{}', '{}'),
       ('alerts/8', '{}', '{}'),
       ('alerts/9', '{}', '{}'),
       ('alerts/10', '{}', '{}');
