package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;

import java.util.Optional;

interface CategoryValueStructuredFactory {

  Optional<CategoryValue> createCategoryValue(
      CategoryValueStructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification);

}
