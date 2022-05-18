UPDATE pb_registered_match prm
SET registered_alert_id = pra.registered_alert_id
FROM pb_registered_alert pra
WHERE pra.alert_message_id = prm.alert_message_id
