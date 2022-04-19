package com.silenteight.scb.outputrecommendation.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.scb-bridge.qco")
public record QcoRecommendationProperties(boolean enabled) {}
