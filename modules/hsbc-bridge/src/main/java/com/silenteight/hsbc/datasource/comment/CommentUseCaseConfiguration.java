package com.silenteight.hsbc.datasource.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class CommentUseCaseConfiguration {

  private final AlertFacade alertFacade;
  private final MatchFacade matchFacade;

  @Bean
  GetCommentInputUseCase getCommentInputUseCase() {
    return new GetCommentInputUseCase(alertFacade, matchFacade);
  }
}
