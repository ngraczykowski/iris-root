SELECT aa.analysis_id,
       ac.category,
       aac.category_id,
       am.match_id,
       am.alert_id
FROM ae_analysis aa
         JOIN ae_analysis_category aac ON aa.analysis_id = aac.analysis_id
                                              AND aa.state = 'PLANNING'
         JOIN ae_category ac ON ac.category_id = aac.category_id
         JOIN ae_analysis_dataset aad ON aa.analysis_id = aad.analysis_id
         JOIN ae_dataset_alert ada ON aad.dataset_id = ada.dataset_id
         JOIN ae_match am ON am.alert_id = ada.alert_id
         LEFT JOIN ae_match_category_value amcv
                   ON amcv.match_id = am.match_id AND amcv.category_id = aac.category_id
WHERE amcv.category_id IS NULL;

