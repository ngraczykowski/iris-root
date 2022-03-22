package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ae.match-recommendation.comment")
@Data
@Validated
class MatchRecommendationCommentProperties {

  private static final String DEFAULT_TEMPLATE_NAME = "alert";

  private String templateName = DEFAULT_TEMPLATE_NAME;
}
