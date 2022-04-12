package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.CommentFacade;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(RecommendationCommentProperties.class)
class GenerateCommentsUseCaseConfiguration {

  private final RecommendationCommentProperties properties;

  @Bean
  GenerateCommentsUseCase generateCommentsUseCase(CommentFacade commentFacade) {
    return new GenerateCommentsUseCase(
        commentFacade, properties.getTemplateName(), properties.getMatchTemplateName());
  }
}
