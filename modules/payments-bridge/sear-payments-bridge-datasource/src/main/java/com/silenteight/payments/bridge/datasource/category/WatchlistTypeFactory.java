package com.silenteight.payments.bridge.datasource.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_WATCHLIST_TYPE;

@Service
@RequiredArgsConstructor
class WatchlistTypeFactory implements CategoryValueUnstructuredFactory {

  @Override
  public Optional<CategoryValue> createCategoryValue(
      CategoryValueUnstructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification) {
    if (!featureInputSpecification.isSatisfy(CATEGORY_NAME_WATCHLIST_TYPE)) {
      return Optional.empty();
    }
    return Optional.of(CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_WATCHLIST_TYPE)
        .setAlert(categoryValueModel.getAlertName())
        .setMatch(categoryValueModel.getMatchName())
        .setSingleValue(categoryValueModel.getWatchlistType().toString())
        .build());
  }
}
