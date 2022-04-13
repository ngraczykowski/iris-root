package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ae.recommendation.comment")
@Data
@Validated
class RecommendationCommentProperties {

  private static final String DEFAULT_TEMPLATE_NAME = "alert";

  private static final String DEFAULT_MATCH_TEMPLATE_NAME = "match-template";

  private String templateName = DEFAULT_TEMPLATE_NAME;

  private String matchTemplateName = DEFAULT_MATCH_TEMPLATE_NAME;

  private boolean shouldGenerateMatchComment = true;
}
