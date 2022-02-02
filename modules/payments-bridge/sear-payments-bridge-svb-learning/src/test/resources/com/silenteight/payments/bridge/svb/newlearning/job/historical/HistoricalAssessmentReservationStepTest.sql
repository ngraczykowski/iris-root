DELETE
FROM pb_learning_alert;
DELETE
FROM pb_learning_hit;
DELETE
FROM pb_learning_action;
DELETE
FROM pb_learning_file;

-- Insert duplicated alerts.
INSERT INTO pb_learning_alert(learning_alert_id, fkco_id, fkco_v_format, fkco_v_system_id,
                              fkco_v_messageid,
                              fkco_d_filtered_datetime, fkco_v_application)
VALUES (10, 1, 'FED', 'systemid', '87AB4899-BE5B-5E4F-E053-150A6C0A7A84', NOW(), 'GFX')
     , (20, 1, 'FED', 'systemid', '87AB4899-BE5B-5E4F-E053-150A6C0A7A85', NOW(), 'GFX')
     , (30, 1, 'FED', 'systemid', '87AB4899-BE5B-5E4F-E053-150A6C0A7A86', NOW(), 'GFX');

-- Insert duplicated hits.
INSERT INTO pb_learning_hit(learning_hit_id, fkco_messages, fkco_v_matched_tag,
                            fkco_v_matched_tag_content, fkco_i_sequence,
                            fkco_v_list_type, fkco_v_list_fmm_id, fkco_v_country_matched_text)
VALUES (10, 1, 'ORIGINATOR', 'AC
888288334211
BOZOABY INC
541 OSSS AVE. STE 100
REDWOOD CITY CA 11063
US', 'sequence', 'INDIVIDUAL', 'shrek', 'GB')
     , (20, 1, 'BENE', 'AC
11820822538185311225
TOMASZ BOZO
AAARBANK
WARSAW', 'sequence', 'INDIVIDUAL', 'shrek', 'GB')
     , (30, 1, '59', '/14446383
GRIFFIN P', 'sequence', 'INDIVIDUAL', 'shrek', 'GB');

-- Insert duplicated actions.
INSERT INTO pb_learning_action(learning_action_id, fkco_messages, fkco_d_action_datetime,
                               fkco_v_status_name, fkco_v_status_behavior)
VALUES (10, 1, CURRENT_TIMESTAMP + (3 || ' minutes')::interval, 'L3_PASS', 'L3_PASS')
     , (20, 1, CURRENT_TIMESTAMP + (2 || ' minutes')::interval, 'L3_PASS', 'L3_PASS')
     , (30, 1, CURRENT_TIMESTAMP, 'L2_L3_PEND', 'L3_PASS');


-- Insert non duplicated hits
INSERT INTO pb_learning_hit(learning_hit_id, fkco_messages, fkco_v_matched_tag,
                            fkco_v_matched_tag_content, fkco_i_sequence, fkco_v_list_type,
                            fkco_v_list_fmm_id)
VALUES (40, 2, 'ORIGINATOR', 'AC
668288334275
WANNABY INC
541 JEFFERSON AVE. STE 100
REDWOOD CITY CA 94063
US', 'sequence1', 'INDIVIDUAL', 'shrek')
     , (50, 2, 'ORIGINATOR', 'AC
668288334275
WANNABY INC
541 JEFFERSON AVE. STE 100
REDWOOD CITY CA 94063
US', 'sequence2', 'INDIVIDUAL', 'shrek')
     , (60, 2, 'ORIGINATOR', 'AC
668288334275
WANNABY INC
541 JEFFERSON AVE. STE 100
REDWOOD CITY CA 94063
US', 'sequence3', 'INDIVIDUAL', 'shrek');
