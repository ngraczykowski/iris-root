package com.silenteight.customerbridge.cbs.gateway;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Collections.emptyMap;

@ConfigurationProperties("serp.scb.bridge.cbs.recommendation")
@Component
@Data
class CbsRecommendationProperties {

  private Map<String, String> recommendationValues = emptyMap();
}
