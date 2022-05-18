package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueUnstructured;

import java.util.Optional;

interface CategoryValueUnstructuredFactory {

  Optional<CategoryValue> createCategoryValue(
      CategoryValueUnstructured categoryValueModel,
      final FeatureInputSpecification featureInputSpecification
  );

}
