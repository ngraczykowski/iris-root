package com.silenteight.adjudication.engine.analysis.matchrecommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.comment.CommentFacade;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(MatchRecommendationCommentProperties.class)
class GenerateMatchCommentsUseCaseConfiguration {

  private final MatchRecommendationCommentProperties properties;

  @Bean
  GenerateMatchCommentsUseCase generateMatchCommentsUseCase(CommentFacade commentFacade) {
    return new GenerateMatchCommentsUseCase(
        commentFacade, properties.getTemplateName(), properties.getMatchTemplateName());
  }
}
