package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class SpecificTermsFactory implements CategoryValueUnstructuredFactory {

  private final SpecificTermsUseCase specificTermsUseCase;

  private static SpecificTermsRequest createRequest(String allMatchingFieldValues) {
    return SpecificTermsRequest
        .builder()
        .allMatchFieldsValue(allMatchingFieldValues)
        .build();
  }

  @Override
  public Optional<CategoryValue> createCategoryValue(
      CategoryValueUnstructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification) {
    if (!featureInputSpecification.isSatisfy(CATEGORY_NAME_SPECIFIC_TERMS)) {
      return Optional.empty();
    }
    var value = specificTermsUseCase
        .invoke(createRequest(categoryValueModel.getAllMatchingFieldValues()))
        .getValue();
    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(value)
        .build());
  }
}
