package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class SpecificTermsProcess extends BaseCategoryValueProcess {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS;
  }

  @Override
  protected String getValue(HitData hitData) {
    return specificTermsUseCase.invoke(createRequest(hitData)).getValue();
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
