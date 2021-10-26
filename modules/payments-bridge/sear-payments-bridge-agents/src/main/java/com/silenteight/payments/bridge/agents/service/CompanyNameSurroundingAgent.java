package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgentGrpc.CompanyNameSurroundingAgentBlockingStub;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CompanyNameSurroundingAgent implements CompanyNameSurroundingUseCase {

  private final CompanyNameSurroundingAgentBlockingStub stub;

  @Setter
  private Duration deadlineDuration = Duration.ofSeconds(10);

  @Override
  public CompanyNameSurroundingAgentResponse invoke(
      CompanyNameSurroundingRequest companyNameSurroundingRequest) {

    var request = getRequest(companyNameSurroundingRequest.getAllNames());

    log.debug("Sending request to Company Name Surrounding Agent");

    var response = stub
        .withDeadlineAfter(deadlineDuration.toMillis(), TimeUnit.MILLISECONDS)
        .checkCompanyNameSurrounding(request);

    if (response == null)
      throw new MissingAgentResultException("Company Name Surrounding Agent");

    if (log.isDebugEnabled()) {
      log.debug(
          "Response received from Company Name Surrounding Agent: {}", response.getSolution());
    }

    return CompanyNameSurroundingAgentResponse.valueOf(response.getSolution());
  }

  private static CheckCompanyNameSurroundingRequest getRequest(List<String> allNames) {
    return CheckCompanyNameSurroundingRequest
        .newBuilder()
        .addAllNames(allNames)
        .build();
  }
}
