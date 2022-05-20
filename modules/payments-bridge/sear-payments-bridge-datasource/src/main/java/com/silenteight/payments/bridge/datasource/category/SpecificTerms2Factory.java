package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS_2;

@Service
@RequiredArgsConstructor
class SpecificTerms2Factory implements CategoryValueUnstructuredFactory {

  private final SpecificTerms2UseCase specificTerms2UseCase;

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
    if (!featureInputSpecification.isSatisfy(CATEGORY_NAME_SPECIFIC_TERMS_2)) {
      return Optional.empty();
    }
    var value = specificTerms2UseCase
        .invoke(createRequest(categoryValueModel.getAllMatchingFieldValues()))
        .getValue();
    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_SPECIFIC_TERMS_2)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(value)
        .build());
  }
}
