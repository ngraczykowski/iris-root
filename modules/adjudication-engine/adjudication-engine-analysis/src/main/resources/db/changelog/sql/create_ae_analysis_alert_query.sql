CREATE
MATERIALIZED VIEW ae_analysis_alert_query AS
SELECT DISTINCT ands.analysis_id, daal.alert_id
FROM ae_analysis_dataset ands
         JOIN ae_dataset_alert daal ON ands.dataset_id = daal.dataset_id
