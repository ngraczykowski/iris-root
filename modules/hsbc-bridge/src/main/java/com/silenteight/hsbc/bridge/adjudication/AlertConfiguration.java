package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.hsbc.bridge.alert.AlertFacade;
import com.silenteight.hsbc.bridge.match.MatchFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertConfiguration {

  @Bean
  AlertService alertService(
      AlertServiceBlockingStub alertServiceBlockingStub, AlertFacade alertFacade,
      MatchFacade matchFacade) {
    return new AlertService(alertServiceBlockingStub, alertFacade, matchFacade);
  }
}
