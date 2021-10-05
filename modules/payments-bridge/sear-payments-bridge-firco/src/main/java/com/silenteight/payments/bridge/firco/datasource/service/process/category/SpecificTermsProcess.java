package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("specificTerms")
@RequiredArgsConstructor
class SpecificTermsProcess implements CategoryValueProcess {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = specificTermsUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/specificTerms")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private SpecificTermsRequest createRequest(HitData hitData) {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(joinMatchingFieldValues(hitData))
        .build();
  }

  @Nonnull
  static String joinMatchingFieldValues(HitData hitData) {
    return String.join(" ", hitData.getHitAndWlPartyData().getAllMatchingFieldValues());
  }
}
