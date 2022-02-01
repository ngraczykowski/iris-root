package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_MATCH_TYPE;

@Service
@RequiredArgsConstructor
class MatchTypeProcess implements CreateCategoryValueUnstructured {

  @Override
  public CategoryValue createCategoryValue(DatasourceUnstructuredModel unstructuredModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_MATCH_TYPE)
        .setAlert(unstructuredModel.getAlertName())
        .setMatch(unstructuredModel.getMatchName())
        .setSingleValue(getValue(unstructuredModel.getHitAndWatchlistPartyData()))
        .build();
  }

  private static String getValue(HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return hitAndWatchlistPartyData.getSolutionType().name();
  }
}
