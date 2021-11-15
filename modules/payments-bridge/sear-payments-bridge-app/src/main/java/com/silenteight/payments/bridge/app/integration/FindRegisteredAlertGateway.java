package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.svb.learning.reader.port.FindRegisteredAlertPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.FindRegisteredAlertEndpoint.FIND_REGISTERED_ALERT_CHANNEL;

@MessagingGateway(defaultRequestChannel = FIND_REGISTERED_ALERT_CHANNEL)
@SuppressWarnings("unused")
interface FindRegisteredAlertGateway extends FindRegisteredAlertPort {}
