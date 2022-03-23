package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_MESSAGE_STRUCTURE;

@Service
@RequiredArgsConstructor
class MessageStructureFactory implements CategoryValueUnstructuredFactory {

  private static String getValue(String tag) {
    return com.silenteight.payments.bridge.common.dto.common.MessageStructure.ofTag(tag).name();
  }

  @Override
  public Optional<CategoryValue> createCategoryValue(
      CategoryValueUnstructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification) {

    if (!featureInputSpecification.isSatisfy(CATEGORY_NAME_MESSAGE_STRUCTURE)) {
      return Optional.empty();
    }

    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_MESSAGE_STRUCTURE)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(getValue(categoryValueModel.getTag()))
        .build());
  }
}
