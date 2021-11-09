package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.data.retention.port.SendAlertsExpiredPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_EXPIRED_OUTBOUND;

@MessagingGateway(defaultRequestChannel = ALERT_EXPIRED_OUTBOUND)
@SuppressWarnings("unused")
interface SendAlertsExpiredGateway extends SendAlertsExpiredPort {}
