package com.silenteight.payments.bridge.app.integration.retention;

import com.silenteight.payments.bridge.data.retention.port.SendPersonalInformationExpiredPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.retention.DataRetentionOutboundAmqpIntegrationConfiguration.PERSONAL_INFORMATION_EXPIRED_OUTBOUND;

@MessagingGateway(defaultRequestChannel = PERSONAL_INFORMATION_EXPIRED_OUTBOUND)
@SuppressWarnings("unused")
interface SendPersonalInformationExpiredGateway extends SendPersonalInformationExpiredPort {}
