UPDATE pb_alert_message_status
  SET delivered_at = COALESCE(recommended_at, rejected_at)
  WHERE delivery_status = 'DELIVERED';
