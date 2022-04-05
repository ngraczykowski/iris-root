package com.silenteight.scb.outputrecommendation.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.cbs")
public record CbsRecommendationMapperConfigurationProperties(boolean attachQcoFieldsToRecom) {
}
