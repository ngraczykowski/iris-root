package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class SpecificTermsProcess implements CreateCategoryValueUnstructured {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  public CategoryValue createCategoryValue(DatasourceUnstructuredModel unstructuredModel) {
    var value = specificTermsUseCase
        .invoke(createRequest(unstructuredModel.getHitAndWatchlistPartyData()))
        .getValue();
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS)
        .setAlert(unstructuredModel.getAlertName())
        .setMatch(unstructuredModel.getMatchName())
        .setSingleValue(value)
        .build();
  }

  @Nonnull
  private static SpecificTermsRequest createRequest(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(joinMatchingFieldValues(hitAndWatchlistPartyData))
        .build();
  }

  @Nonnull
  private static String joinMatchingFieldValues(HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return String.join(" ", hitAndWatchlistPartyData.getAllMatchingFieldValues());
  }
}
