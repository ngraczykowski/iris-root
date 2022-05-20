SELECT DISTINCT ad.analysis_id, d.dataset_id, count(da.alert_id) as alert_count
FROM ae_analysis_dataset ad
         LEFT JOIN ae_dataset d ON ad.dataset_id = d.dataset_id
         LEFT JOIN ae_dataset_alert da ON da.dataset_id = d.dataset_id
GROUP BY ad.analysis_id, d.dataset_id;
