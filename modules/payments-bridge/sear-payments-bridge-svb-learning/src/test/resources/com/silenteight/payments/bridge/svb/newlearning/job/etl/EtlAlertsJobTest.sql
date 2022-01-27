INSERT INTO public.pb_learning_alert (learning_alert_id, fkco_id, fkco_v_system_id, fkco_v_format,
                                      fkco_v_type, fkco_v_transaction_ref, fkco_v_related_ref,
                                      fkco_v_sens, fkco_v_business_unit, fkco_v_application,
                                      fkco_v_currency, fkco_f_amount, fkco_v_content,
                                      fkco_b_highlight_all, fkco_v_value_date, fkco_unit,
                                      fkco_i_msg_fml_priority, fkco_i_msg_fml_confidentiality,
                                      fkco_d_app_deadline, fkco_i_app_priority, fkco_i_normamount,
                                      fkco_v_messageid, fkco_v_copy_service,
                                      fkco_d_filtered_datetime, fkco_i_blockinghits)
VALUES (1, 102925, 'charlie', 'IAT-O', 'IAT', '11859', 'N/A', 'O', 'NACHA', 'PEP', 'USD', '155.27', '[FIRCOSOFT     X] SVB BE MUX FILTER
[APPLI         X] PEP
[UNIT          X] US
[REFERENCE     X] 0011859
[TYPE          X] IAT-O
[VALUEDATE     X] 2019/01/01
[PURPOSE        ] IAT PAYPAL
[AMOUNT        X] USD 1.5527000000000002
[INSPMTAMT     X] USD 10
[SENDER         ] 25317006
[SENDERBANK     ] 25317006
TEST SYSTEM BANK1
01
AutoReceiver-G23GM6LOM
US
[ORIGINATOR     ] AutoReceiver-G23GM6LOM
JOHN SMITH
FLAT 1 4, 15AOSWALD STREET
GLASGOW
GB
[CRDACCNUM      ] 3300684802
[RECEIVBANK     ] AutoReceiver-G23GM6LOM
TEST SYSTEM BANK2
01
AutoReceiver-G23GM6LOM
US
[RECEIVER       ] AutoReceiver-G23GM6LOM
[BENE           ] AutoReceiver-G23GM6LOM
Saddam Hussain
316 MAIN STREET
PGH
US
[ORGBENEINF     ] value1
value2
value3
[TRANCD        X] 27
[ITMBANK        ] Adam
1
AutoReceiver-G23GM6LOM
US
[ITMBANK1       ] Boris
2
AutoReceiver-G23GM6LOM
UK
[FXMATH        X] FF
[FXREFNB       X]
[ADDENDACNT    X] 0007
[DESTCNTRY      ] US
[ORIGSTSCD     X] 1', 'N/A', '2019/01/01', '36', '0', '0', '', '0', '0', 'unicorn', 'N/A',
        '1999-01-01 14:52:45.000000', '11');
INSERT INTO public.pb_learning_alert (learning_alert_id, fkco_id, fkco_v_system_id, fkco_v_format,
                                      fkco_v_type, fkco_v_transaction_ref, fkco_v_related_ref,
                                      fkco_v_sens, fkco_v_business_unit, fkco_v_application,
                                      fkco_v_currency, fkco_f_amount, fkco_v_content,
                                      fkco_b_highlight_all, fkco_v_value_date, fkco_unit,
                                      fkco_i_msg_fml_priority, fkco_i_msg_fml_confidentiality,
                                      fkco_d_app_deadline, fkco_i_app_priority, fkco_i_normamount,
                                      fkco_v_messageid, fkco_v_copy_service,
                                      fkco_d_filtered_datetime, fkco_i_blockinghits)
VALUES (2, 102925, 'DIN20190429085242-00061-24304', 'IAT-O', 'IAT', '11859', 'N/A', 'O', 'NACHA',
        'PEP', 'USD', '155.27', '[FIRCOSOFT     X] SVB BE MUX FILTER
[APPLI         X] PEP
[UNIT          X] US
[REFERENCE     X] 0011859
[TYPE          X] IAT-O
[VALUEDATE     X] 2019/01/01
[PURPOSE        ] IAT PAYPAL
[AMOUNT        X] USD 1.5527000000000002
[INSPMTAMT     X] USD 10
[SENDER         ] 25317006
[SENDERBANK     ] 25317006
TEST SYSTEM BANK1
01
AutoReceiver-G23GM6LOM
US
[ORIGINATOR     ] AutoReceiver-G23GM6LOM
JOHN SMITH
FLAT 1 4, 15AOSWALD STREET
GLASGOW
GB
[CRDACCNUM      ] 3300684802
[RECEIVBANK     ] AutoReceiver-G23GM6LOM
TEST SYSTEM BANK2
01
AutoReceiver-G23GM6LOM
US
[RECEIVER       ] AutoReceiver-G23GM6LOM
[BENE           ] AutoReceiver-G23GM6LOM
Saddam Hussain
316 MAIN STREET
PGH
US
[ORGBENEINF     ] value1
value2
value3
[TRANCD        X] 27
[ITMBANK        ] Adam
1
AutoReceiver-G23GM6LOM
US
[ITMBANK1       ] Boris
2
AutoReceiver-G23GM6LOM
UK
[FXMATH        X] FF
[FXREFNB       X]
[ADDENDACNT    X] 0007
[DESTCNTRY      ] US
[ORIGSTSCD     X] 1', 'N/A', '2019/01/01', '36', '0', '0', '', '0', '0',
        '87AB4899-BE5B-5E4F-E053-150A6C0A7A84', 'N/A', '2019-04-29 13:52:45.000000', '11');

INSERT INTO public.pb_learning_hit (learning_hit_id, fkco_messages, fkco_b_highlight_hit,
                                    fkco_v_name_matched_text, fkco_v_address_matched_text,
                                    fkco_v_city_matched_text, fkco_v_state_matched_text,
                                    fkco_v_country_matched_text, fkco_v_list_matched_name,
                                    fkco_v_fml_type, fkco_i_fml_priority,
                                    fkco_i_fml_confidentiality, fkco_v_hit_match_level,
                                    fkco_v_hit_type, fkco_i_nonblocking, fkco_i_blocking,
                                    fkco_listed_record, fkco_filtered_date,
                                    fkco_d_filtered_datetime_1, fkco_v_matched_tag,
                                    fkco_v_matched_tag_content, fkco_i_sequence, fkco_v_list_fmm_id,
                                    fkco_v_list_official_ref, fkco_v_list_type, fkco_v_list_origin,
                                    fkco_v_list_designation, fkco_v_list_pep, fkco_v_list_fep,
                                    fkco_v_list_name, fkco_v_list_city, fkco_v_list_state,
                                    fkco_v_list_country, fkco_v_list_userdata1,
                                    fkco_v_list_userdata2, fkco_v_list_keyword,
                                    fkco_v_list_add_info)
VALUES (2, 102925, 'N/A', 'JOHN SMITH', 'GB', 'N/A', 'N/A', 'N/A', 'PRIDMORE-SMITH, JOHN B.',
        'BLOCKING', '0', '0', '0', 'NAME', '0', '1', '32900', '7059', '29-Apr-19', 'ORIGINATOR', 'AutoReceiver-G23GM6LOM
JOHN SMITH
FLAT 1 4, 15AOSWALD STREET
GLASGOW
GB', '1', 'shrekMatchId', '2002-09-20 00:00:00 BIS', 'INDIVIDUAL', 'US_BIS', 'DENIED_PERSONS_LIST',
        '0', '0', 'PRIDMORE-SMITH, JOHN B.', 'DAVENTRY', 'N/A', 'UNITED KINGDOM', 'N/A', 'N/A',
        'OS:BIS',
        'List ID: 28 / Create Date: 02/26/2002 00:00:00 / Last Update Date: 04/16/2013 17:30:54 / Program: DENIED PERSONS LIST / FederalRegisterCitation: 49 F.R. 49666 OtherInfo: 12/21/84 / EffectiveDate: 12/14/84 / Typeofdenial: STANDARD');

INSERT INTO public.pb_learning_action (learning_action_id, fkco_messages, fkco_v_action_comment,
                                       fkco_action_date, fkco_d_action_datetime, fkco_operator,
                                       fkco_status, fkco_i_total_action, fkco_v_status_name,
                                       fkco_v_status_behavior)
VALUES (1, 102925, 'Automation Comment - FR FAIL', '7059', '2019-04-29 13:54:16.000000', '9196',
        '23', '1', 'L3_BLOCK', 'PENDING');
INSERT INTO public.pb_learning_action (learning_action_id, fkco_messages, fkco_v_action_comment,
                                       fkco_action_date, fkco_d_action_datetime, fkco_operator,
                                       fkco_status, fkco_i_total_action, fkco_v_status_name,
                                       fkco_v_status_behavior)
VALUES (2, 102925, 'Automation Comment - FR FAIL', '7059', '2019-05-29 13:54:16.000000', '9196',
        '23', '1', 'L3_BLOCK', 'PENDING');
INSERT INTO public.pb_learning_action (learning_action_id, fkco_messages, fkco_v_action_comment,
                                       fkco_action_date, fkco_d_action_datetime, fkco_operator,
                                       fkco_status, fkco_i_total_action, fkco_v_status_name,
                                       fkco_v_status_behavior)
VALUES (3, 102925, 'Automation Comment - FR FAIL', '7059', '2019-06-29 13:54:16.000000', '9196',
        '23', '1', 'FR_FAIL', 'PENDING');
INSERT INTO public.pb_learning_action (learning_action_id, fkco_messages, fkco_v_action_comment,
                                       fkco_action_date, fkco_d_action_datetime, fkco_operator,
                                       fkco_status, fkco_i_total_action, fkco_v_status_name,
                                       fkco_v_status_behavior)
VALUES (4, 102925, 'Automation Comment - FR FAIL', '7059', '2019-07-29 13:54:16.000000', '9196',
        '23', '1', 'FR_FAIL', 'PENDING');


INSERT INTO public.pb_learning_etl_reservation (learning_alert_id, job_id) VALUES (1, 1);
INSERT INTO public.pb_learning_etl_reservation (learning_alert_id, job_id) VALUES (2, 1);
