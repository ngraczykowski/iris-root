package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS_2;

@Service
@RequiredArgsConstructor
class SpecificTerms2Factory implements CategoryValueUnstructuredFactory {

  private final SpecificTerms2UseCase specificTerms2UseCase;

  @Override
  public CategoryValue createCategoryValue(CategoryValueUnstructured categoryValueModel) {
    var value = specificTerms2UseCase
        .invoke(createRequest(categoryValueModel.getAllMatchingFieldValues()))
        .getValue();
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS_2)
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
