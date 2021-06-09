package com.silenteight.hsbc.bridge.recommendation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

import static java.util.Collections.emptyMap;

@ConfigurationProperties("silenteight.bridge.recommendation")
@Data
@ConstructorBinding
class RecommendationProperties {

  private Map<S8Recommendation, String> silentEightValues = emptyMap();
  private Map<HsbcRecommendation, String> userFriendlyValues = emptyMap();
}
