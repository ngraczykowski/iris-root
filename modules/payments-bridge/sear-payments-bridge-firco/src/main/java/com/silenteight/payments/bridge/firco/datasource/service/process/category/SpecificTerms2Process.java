package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@RequiredArgsConstructor
class SpecificTerms2Process extends BaseCategoryValueProcess {

  public static final String CATEGORY_SPECIFIC_TERMS_2 = "specificTerms2";

  private final SpecificTerms2UseCase specificTerms2UseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS_2;
  }

  @Override
  protected String getValue(HitData hitData) {
    return specificTerms2UseCase.invoke(createRequest(hitData)).getValue();
  }

  @Nonnull
  private static SpecificTermsRequest createRequest(HitData hitData) {
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
