package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.MatchtextFirstTokenOfAddressAgentRequest;
import com.silenteight.payments.bridge.agents.port.MatchTextFirstTokenOfAddressUseCase;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("firstTokenAddress")
@RequiredArgsConstructor
class MatchTextFirstTokenOfAddressProcess implements CategoryValueProcess {

  private final MatchTextFirstTokenOfAddressUseCase matchTextFirstTokenOfAddressUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = matchTextFirstTokenOfAddressUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/firstTokenAddress")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private MatchtextFirstTokenOfAddressAgentRequest createRequest(HitData hitData) {
    return MatchtextFirstTokenOfAddressAgentRequest.builder()
        .matchingTexts(hitData.getHitAndWlPartyData().getAllMatchingTexts())
        .addresses(hitData.getAlertedPartyData().getAddresses())
        .build();
  }
}
