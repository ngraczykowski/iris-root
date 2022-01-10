UPDATE pb_registered_alert pra
SET fkco_message_id = pam.message_id,
    fkco_system_id = pam.system_id
FROM pb_alert_message pam
WHERE pam.alert_message_id = pra.alert_message_id
