CREATE TRIGGER ae_analysis_alert_dataset_trigger AFTER INSERT OR DELETE ON ae_analysis_dataset FOR EACH STATEMENT EXECUTE PROCEDURE refresh_ae_analysis_alert_query();
