package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificCommonTermsUseCase;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.firco.datasource.service.process.category.SpecificTermsProcess.joinMatchingFieldValues;

@Service
@Qualifier("specificCommonTerms")
@RequiredArgsConstructor
class SpecificCommonTermsProcess implements CategoryValueProcess {

  private final SpecificCommonTermsUseCase specificCommonTermsUseCase;

  @Override
  public CategoryValue extract(AlertRegisteredEvent data, HitData hitData, String matchValue) {
    var value = specificCommonTermsUseCase.invoke(createRequest(hitData));
    return CategoryValue
        .newBuilder()
        .setName("categories/specificCommonTerms")
        .setMatch(matchValue)
        .setSingleValue(value.toString())
        .build();
  }

  @Nonnull
  private SpecificCommonTermsRequest createRequest(HitData hitData) {
    return SpecificCommonTermsRequest
        .builder()
        .isAccountNumberFlagInMatchingField(
            hitData.getHitAndWlPartyData().getMessageStructure().checkMessageWithoutAccountNum())
        .allMatchFieldsValue(joinMatchingFieldValues(hitData))
        .build();
  }
}
