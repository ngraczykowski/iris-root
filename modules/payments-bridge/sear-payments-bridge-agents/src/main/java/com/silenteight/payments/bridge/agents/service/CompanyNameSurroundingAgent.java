package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CheckCompanyNameSurroundingRequest;
import com.silenteight.proto.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgentGrpc.CompanyNameSurroundingAgentBlockingStub;

import io.grpc.StatusRuntimeException;

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

    try {
      var response = stub
          .withDeadlineAfter(deadlineDuration.toMillis(), TimeUnit.MILLISECONDS)
          .checkCompanyNameSurrounding(request);

      if (response.getSolution().isEmpty()) {
        log.error(
            "Company Name Surrounding Agent returned empty solution: result={}",
            response.getResult());
        return CompanyNameSurroundingAgentResponse.AGENT_ERROR;
      }

      if (log.isDebugEnabled()) {
        log.debug(
            "Response received from Company Name Surrounding Agent: solution={}, result={}",
            response.getSolution(), response.getResult());
      }

      return CompanyNameSurroundingAgentResponse.valueOf(response.getSolution());
    } catch (StatusRuntimeException e) {
      var status = e.getStatus();
      log.error("Failed to call Company Name Surrounding Agent: code={}, description={})",
          status.getCode(), status.getDescription());
      return CompanyNameSurroundingAgentResponse.AGENT_ERROR;
    } catch (Exception e) {
      log.error("Unable to get Company Name Surrounding response", e);
      return CompanyNameSurroundingAgentResponse.AGENT_ERROR;
    }
  }

  private static CheckCompanyNameSurroundingRequest getRequest(List<String> allNames) {
    return CheckCompanyNameSurroundingRequest
        .newBuilder()
        .addAllNames(allNames)
        .build();
  }
}
