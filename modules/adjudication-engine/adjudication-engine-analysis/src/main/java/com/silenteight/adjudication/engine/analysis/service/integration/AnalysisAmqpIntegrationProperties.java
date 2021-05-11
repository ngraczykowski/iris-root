package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "ae.analysis.integration")
class AnalysisAmqpIntegrationProperties {
}
