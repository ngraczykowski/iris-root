package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.OneLinerAgentRequest;
import com.silenteight.payments.bridge.agents.port.OneLinerUseCase;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("oneliner")
@RequiredArgsConstructor
class OneLinerProcess implements CategoryValueProcess {

  private final OneLinerUseCase oneLinerUseCase;

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    var value = oneLinerUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/oneLiner")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private OneLinerAgentRequest createRequest(HitData hitData) {
    return OneLinerAgentRequest.builder()
        .noAcctNumFlag(hitData.getAlertedPartyData().isNoAcctNumFlag())
        .noOfLines(hitData.getAlertedPartyData().getNumOfLines())
        .messageLength(hitData.getAlertedPartyData().getMessageLength())
        .build();
  }
}
