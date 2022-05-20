package com.silenteight.hsbc.bridge.recommendation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import javax.validation.constraints.NotEmpty;

@Validated
@ConfigurationProperties("silenteight.bridge.recommendation")
@Data
@ConstructorBinding
class RecommendationProperties {

  @NotEmpty
  private Map<S8Recommendation, String> silentEightValues;

  @NotEmpty
  private Map<HsbcRecommendation, String> userFriendlyValues;
}
