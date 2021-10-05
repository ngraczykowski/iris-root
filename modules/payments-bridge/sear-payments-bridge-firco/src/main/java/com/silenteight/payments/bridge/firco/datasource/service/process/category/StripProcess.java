package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.port.StripUseCase;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("strip")
@RequiredArgsConstructor
class StripProcess implements CategoryValueProcess {

  private final StripUseCase stripUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = stripUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/strip")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private String createRequest(HitData hitData) {
    return hitData.getHitAndWlPartyData().getId();
  }
}
