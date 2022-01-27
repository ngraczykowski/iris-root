package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_MATCH_TYPE;

@Service
@RequiredArgsConstructor
class MatchTypeProcess implements CategoryValueProcess {

  @Override
  public CategoryValue createCategoryValue(CategoryValueExtractModel categoryValueExtractModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_MATCH_TYPE)
        .setAlert(categoryValueExtractModel.getAlertName())
        .setMatch(categoryValueExtractModel.getMatchName())
        .setSingleValue(getValue(categoryValueExtractModel.getHitData()))
        .build();
  }

  private static String getValue(HitData hitData) {
    return hitData.getHitAndWlPartyData().getSolutionType().name();
  }
}
