package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import javax.annotation.Nonnull;

@Service
@Qualifier("historicalRiskAssessment")
@RequiredArgsConstructor
class HistoricalRiskAssessmentProcess implements CategoryValueProcess {

  private final HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase;

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    var value = historicalRiskAssessmentUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/historicalRiskAssessment")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private HistoricalRiskAssessmentAgentRequest createRequest(HitData hitData) {

    var alertedPartyData = hitData.getAlertedPartyData();

    String name = Optional.of(alertedPartyData)
        .map(AlertedPartyData::getNames)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");

    String accountNumberOrName = Optional.of(alertedPartyData)
        .map(AlertedPartyData::getAccountNumber)
        .filter(accountNumber -> !accountNumber.isBlank())
        .orElse(name);

    return HistoricalRiskAssessmentAgentRequest
        .builder()
        .accountNumber(accountNumberOrName)
        .ofacID(hitData.getHitAndWlPartyData().getId())
        .build();
  }
}
