package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CommentUseCaseConfiguration {

  private final MatchFacade matchFacade;

  @Bean
  GetCommentInputUseCase getCommentInputUseCase() {
    return new GetCommentInputUseCase(matchFacade);
  }
}
