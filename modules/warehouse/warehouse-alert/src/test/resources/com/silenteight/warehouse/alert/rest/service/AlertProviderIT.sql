insert INTO warehouse_alert(discriminator, name, recommendation_date, payload)
VALUES ('1ed644fd-3b38-4626-8977-a5bf41ade2a5','21dd1cd2-42b9-43c0-9d97-8ac379b364d1','2021-12-23 13:42:45.317941','{"id": "12345", "step": "steps/5ca9d972-695b-4550-a383-b09311ff42e1", "s8_lobCountryCode": "UK", "analyst_decision": "analyst_decision_false_positive", "qa.level-0.state": "qa_decision_PASSED"}'::jsonb) ON CONFLICT DO NOTHING;

insert INTO warehouse_alert(discriminator, name, recommendation_date, payload)
VALUES ('1ed644fd-3b38-4626-8977-a5bf41ade2a4','21dd1cd2-42b9-43c0-9d97-8ac379b364d2','2021-12-23 13:42:45.317941','{"id": "12345", "step": "steps/5ca9d972-695b-4550-a383-b09311ff42e2", "s8_lobCountryCode": "PL", "analyst_decision": "analyst_decision_true_positive", "qa.level-0.state": "qa_decision_PASSED"}'::jsonb) ON CONFLICT DO NOTHING;

insert INTO warehouse_alert(discriminator, name, recommendation_date, payload)
VALUES ('1ed644fd-3b38-4626-8977-a5bf41ade2a3','21dd1cd2-42b9-43c0-9d97-8ac379b364d3','2021-12-23 13:42:45.317941','{"id": "12345", "step": "steps/5ca9d972-695b-4550-a383-b09311ff42e3", "s8_lobCountryCode": "DE", "analyst_decision": "analyst_decision_false_positive", "qa.level-0.state": "qa_decision_PASSED"}'::jsonb) ON CONFLICT DO NOTHING;

insert INTO warehouse_alert(discriminator, name, recommendation_date, payload)
VALUES ('1ed644fd-3b38-4626-8977-a5bf41ade2a2','21dd1cd2-42b9-43c0-9d97-8ac379b364d4','2021-12-23 13:42:45.317941','{"id": "12345", "step": "steps/5ca9d972-695b-4550-a383-b09311ff42e4", "s8_lobCountryCode": "UK", "analyst_decision": "analyst_decision_true_positive", "qa.level-0.state": "qa_decision_PASSED"}'::jsonb) ON CONFLICT DO NOTHING;

insert INTO warehouse_alert(discriminator, name, recommendation_date, payload)
VALUES ('1ed644fd-3b38-4626-8977-a5bf41ade2a1','21dd1cd2-42b9-43c0-9d97-8ac379b364d5','2021-12-23 13:42:45.317941','{"id": "12345", "step": "steps/5ca9d972-695b-4550-a383-b09311ff42e4", "s8_lobCountryCode": "PL", "analyst_decision": "analyst_decision_false_positive", "qa.level-0.state": "qa_decision_PASSED"}'::jsonb) ON CONFLICT DO NOTHING;
