package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured.AlertedData;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_COMPANY_NAME_SURROUNDING;
import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class CompanyNameSurroundingFactory implements CategoryValueStructuredFactory {

  private final CompanyNameSurroundingUseCase companyNameSurroundingUseCase;

  @Override
  public Optional<CategoryValue> createCategoryValue(CategoryValueStructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification) {
    if (!featureInputSpecification.isSatisfy(CATEGORY_NAME_SPECIFIC_TERMS)) {
      return Optional.empty();
    }
    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_COMPANY_NAME_SURROUNDING)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(getValue(categoryValueModel.getAlertedData()))
        .build());
  }

  private String getValue(AlertedData alertedData) {
    var alertedPartyNames =
        Optional.ofNullable(alertedData.getNames()).orElseGet(
            Collections::emptyList);

    return companyNameSurroundingUseCase.invoke(
        CompanyNameSurroundingRequest.builder().allNames(alertedPartyNames).build()).name();
  }
}
