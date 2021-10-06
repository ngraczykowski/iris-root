package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.TwoLinesNameAgentRequest;
import com.silenteight.payments.bridge.agents.port.TwoLinesNameUseCase;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("twoLines")
@RequiredArgsConstructor
class TwoLinesNameProcess implements CategoryValueProcess {

  private final TwoLinesNameUseCase twoLinesNameUseCase;

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    var value = twoLinesNameUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("category/twoLines")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private TwoLinesNameAgentRequest createRequest(HitData hitData) {
    return TwoLinesNameAgentRequest
        .builder()
        .alertedPartyAddresses(hitData.getAlertedPartyData().getAddresses())
        .build();
  }
}
