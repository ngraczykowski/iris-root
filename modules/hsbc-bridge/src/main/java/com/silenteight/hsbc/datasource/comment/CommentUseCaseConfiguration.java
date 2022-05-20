package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = { CommentProperties.class })
@RequiredArgsConstructor
class CommentUseCaseConfiguration {

  private final MatchFacade matchFacade;

  @Bean
  GetCommentInputUseCase getCommentInputUseCase(CommentProperties commentProperties) {
    return new GetCommentInputUseCase(matchFacade, commentProperties.getWlTypes());
  }
}
