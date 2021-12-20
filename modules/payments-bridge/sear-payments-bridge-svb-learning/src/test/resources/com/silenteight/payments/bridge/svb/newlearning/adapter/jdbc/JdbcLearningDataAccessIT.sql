-- Insert duplicated alerts.
INSERT INTO pb_learning_alert(learning_alert_id, fkco_id, fkco_v_system_id)
VALUES (10, 1, 'systemid')
     , (20, 1, 'systemid')
     , (30, 1, 'systemid');

-- Insert duplicated hits.
INSERT INTO pb_learning_hit(learning_hit_id, fkco_messages, fkco_v_matched_tag, fkco_i_sequence, fkco_v_list_fmm_id)
VALUES (10, 1, 'tag', 'sequence', 'fmm_id')
     , (20, 1, 'tag', 'sequence', 'fmm_id')
     , (30, 1, 'tag', 'sequence', 'fmm_id');

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
