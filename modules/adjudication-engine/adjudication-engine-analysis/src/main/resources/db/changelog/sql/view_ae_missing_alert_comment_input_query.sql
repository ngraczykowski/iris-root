SELECT ada.alert_id, aa.analysis_id
FROM ae_analysis aa
         JOIN ae_analysis_dataset aad ON aa.analysis_id = aad.analysis_id
         JOIN ae_dataset_alert ada ON aad.dataset_id = ada.dataset_id
         LEFT JOIN ae_alert_comment_input aaci
                   ON aaci.alert_id = ada.alert_id
WHERE aaci.alert_id IS NULL;
