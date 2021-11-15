package com.silenteight.payments.bridge.app.integration.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.app.integration.ChannelFactory;
import com.silenteight.payments.bridge.app.integration.model.CreateAnalysisRequestMapper;
import com.silenteight.payments.bridge.governance.solvingmodel.port.GetCurrentProductionModelUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;

@MessageEndpoint
@RequiredArgsConstructor
class BuildCreateAnalysisRequestReplyEndpoint {

  static final String BUILD_CREATE_ANALYSIS_CHANNEL = "buildCreateAnalysisRequestChannel";

  private final GetCurrentProductionModelUseCase getCurrentProductionModelUseCase;
  private final CreateAnalysisRequestMapper createAnalysisRequestMapper;

  @ServiceActivator(inputChannel = BUILD_CREATE_ANALYSIS_CHANNEL)
  CreateAnalysisRequest build() {
    var model = getCurrentProductionModelUseCase.getModel();
    return createAnalysisRequestMapper.map(model);
  }

  @Bean(BUILD_CREATE_ANALYSIS_CHANNEL)
  MessageChannel messageChannel() {
    return ChannelFactory.createDirectChannel();
  }

}
