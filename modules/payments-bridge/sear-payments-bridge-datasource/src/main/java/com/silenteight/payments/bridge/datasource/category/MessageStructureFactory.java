package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_MESSAGE_STRUCTURE;

@Service
@RequiredArgsConstructor
class MessageStructureFactory implements CategoryValueUnstructuredFactory {

  @Override
  public CategoryValue createCategoryValue(CategoryValueUnstructured categoryValueModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_MESSAGE_STRUCTURE)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(getValue(categoryValueModel.getTag()))
        .build();
  }

  private static String getValue(String tag) {
    return com.silenteight.payments.bridge.common.dto.common.MessageStructure.ofTag(tag).name();
  }
}
