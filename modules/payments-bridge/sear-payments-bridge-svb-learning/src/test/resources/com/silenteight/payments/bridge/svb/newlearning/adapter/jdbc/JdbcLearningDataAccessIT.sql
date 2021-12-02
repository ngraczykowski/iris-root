-- Insert duplicated alerts.
INSERT INTO pb_learning_alert(learning_alert_id, fkco_id)
VALUES (10, 1)
     , (20, 1)
     , (30, 1);

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

-- Insert duplicated listed records.
INSERT INTO pb_learning_listed_record(learning_listed_record_id, fkco_id, fkco_v_list_fmm_id)
VALUES (10, 1, 'fmm')
     , (20, 1, 'fmm')
     , (30, 1, 'fmm');

-- Insert duplicated action status.
INSERT INTO pb_learning_action_status(learning_action_status_id, fkco_id, fkco_v_status_name,
                                      fkco_v_status_behavior)
VALUES (10, 1, 'fmm', 'behavior')
     , (20, 1, 'fmm', 'behavior')
     , (30, 1, 'fmm', 'behavior');

-- Insert duplicated alerted messages.
INSERT INTO pb_learning_alerted_message(learning_alerted_message_id, fkco_messages,
                                        fkco_d_filtered_datetime,
                                        fkco_i_blockinghits)
VALUES (10, 1, now(), 'blockinghits')
     , (20, 1, now(), 'blockinghits')
     , (30, 1, now(), 'blockinghits');



