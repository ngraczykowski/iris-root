-- alert_retention
INSERT INTO pb_alert_data_retention VALUES('alerts/1', now() - '1h'::interval, now() - '1h'::interval, NULL, NULL);
INSERT INTO pb_alert_data_retention VALUES('alerts/2', now() - '49h'::interval, now() - '49h'::interval, NULL, now());
INSERT INTO pb_alert_data_retention VALUES('alerts/3', now() - '73d'::interval, now() - '73h'::interval, now(), NULL);
