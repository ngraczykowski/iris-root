INSERT INTO ae_analysis_alert
  (SELECT analysis_id, alert_id, NULL, now()
   FROM ae_analysis_alert_query);
