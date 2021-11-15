package com.silenteight.payments.bridge.app.integration.retention;

import com.silenteight.payments.bridge.svb.learning.reader.port.CreateAlertRetentionPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.retention.CreateAlertRetentionEndpoint.CREATE_ALERT_RETENTION_CHANNEL;

@MessagingGateway(defaultRequestChannel = CREATE_ALERT_RETENTION_CHANNEL)
@SuppressWarnings("unused")
interface CreateAlertRetentionGateway extends CreateAlertRetentionPort {}
