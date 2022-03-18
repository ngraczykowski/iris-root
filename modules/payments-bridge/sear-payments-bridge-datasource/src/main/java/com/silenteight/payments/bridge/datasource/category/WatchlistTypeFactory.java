package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_WATCHLIST_TYPE;

@Service
@RequiredArgsConstructor
class WatchlistTypeFactory implements CategoryValueUnstructuredFactory {

  @Override
  public CategoryValue createCategoryValue(CategoryValueUnstructured categoryValueModel) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_WATCHLIST_TYPE)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(categoryValueModel.getWatchlistType().toString())
        .build();
  }
}
