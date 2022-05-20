package com.silenteight.payments.bridge.app.integration;

import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@EnableIntegration
@EnableIntegrationGraphController
@IntegrationComponentScan
@Configuration(proxyBeanMethods = false)
class IntegrationConfiguration {
}
