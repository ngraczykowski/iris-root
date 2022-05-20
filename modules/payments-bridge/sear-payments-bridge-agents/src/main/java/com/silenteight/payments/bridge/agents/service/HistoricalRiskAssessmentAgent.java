package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentResponse;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;

import java.util.List;
import javax.annotation.Nonnull;

@Slf4j
class HistoricalRiskAssessmentAgent implements HistoricalRiskAssessmentUseCase {

  private final List<ConfigTuple> configList;


  HistoricalRiskAssessmentAgent(
      List<ConfigTuple> configList) {
    this.configList = configList;
    log.debug("HistoricalRiskAssessmentAgent initialized with {} entries", configList.size());
  }

  @Override
  public HistoricalRiskAssessmentAgentResponse invoke(
      @NonNull HistoricalRiskAssessmentAgentRequest historicalRiskAssessmentAgentRequest) {
    String accountNumberTrimmed =
        historicalRiskAssessmentAgentRequest.getAccountNumber().toUpperCase().trim();
    String ofacIdTrimmed = historicalRiskAssessmentAgentRequest.getOfacID().toUpperCase().trim();

    HistoricalRiskAssessmentAgentResponse response = getHistoricalRiskAssessmentAgentResponse(
        accountNumberTrimmed, ofacIdTrimmed);

    if (log.isTraceEnabled()) {
      log.trace(
          "invoked. [accountNumber={}], [ofacID={}]. Result={}",
          historicalRiskAssessmentAgentRequest.getAccountNumber(),
          historicalRiskAssessmentAgentRequest.getOfacID(),
          response);
    }
    return response;
  }

  @Nonnull
  private HistoricalRiskAssessmentAgentResponse getHistoricalRiskAssessmentAgentResponse(
      String accountNumberTrimmed, String ofacIdTrimmed) {
    for (var configEntry : configList) {
      String configListAccNo = configEntry.accountNumber.toUpperCase().trim();
      String configListOfacID = configEntry.ofacId.toUpperCase().trim();

      if (configListAccNo.equals(accountNumberTrimmed) &&
          configListOfacID.equals(ofacIdTrimmed)) {
        return HistoricalRiskAssessmentAgentResponse.YES;
      }
    }
    return HistoricalRiskAssessmentAgentResponse.NO;
  }

  @Value
  static class ConfigTuple {

    String accountNumber;
    String ofacId;
  }
}
