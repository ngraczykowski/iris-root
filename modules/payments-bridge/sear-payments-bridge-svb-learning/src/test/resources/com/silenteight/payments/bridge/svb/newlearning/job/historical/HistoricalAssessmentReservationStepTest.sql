DELETE
FROM pb_learning_alert;
DELETE
FROM pb_learning_hit;
DELETE
FROM pb_learning_action;
DELETE
FROM pb_learning_file;

-- Insert duplicated alerts.
INSERT INTO pb_learning_alert(learning_alert_id, fkco_id, fkco_v_system_id, fkco_d_filtered_datetime)
VALUES (10, 1, 'systemid', now())
     , (20, 1, 'systemid', now())
     , (30, 1, 'systemid', now());

-- Insert duplicated hits.
INSERT INTO pb_learning_hit(learning_hit_id, fkco_messages, fkco_v_matched_tag, fkco_i_sequence)
VALUES (10, 1, 'tag', 'sequence')
     , (20, 1, 'tag', 'sequence')
     , (30, 1, 'tag', 'sequence');

-- Insert duplicated actions.
INSERT INTO pb_learning_action(learning_action_id, fkco_messages, fkco_d_action_datetime)
VALUES (10, 1, now())
     , (20, 1, now())
     , (30, 1, now());


-- Insert non duplicated hits
INSERT INTO pb_learning_hit(learning_hit_id, fkco_messages, fkco_v_matched_tag, fkco_i_sequence)
VALUES (40, 2, 'tag', 'sequence1')
     , (50, 2, 'tag', 'sequence2')
     , (60, 2, 'tag', 'sequence3');
