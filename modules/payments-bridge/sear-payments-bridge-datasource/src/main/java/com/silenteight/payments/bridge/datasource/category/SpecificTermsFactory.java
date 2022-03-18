package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class SpecificTermsFactory implements CategoryValueUnstructuredFactory {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  public CategoryValue createCategoryValue(CategoryValueUnstructured categoryValueModel) {
    var value = specificTermsUseCase
        .invoke(createRequest(categoryValueModel.getAllMatchingFieldValues()))
        .getValue();
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(value)
        .build();
  }

  private static SpecificTermsRequest createRequest(String allMatchingFieldValues) {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(allMatchingFieldValues)
        .build();
  }
}
