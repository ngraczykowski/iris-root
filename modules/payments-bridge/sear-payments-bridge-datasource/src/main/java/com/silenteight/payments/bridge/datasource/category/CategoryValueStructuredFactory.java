package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.category.dto.CategoryValueStructured;

interface CategoryValueStructuredFactory {

  CategoryValue createCategoryValue(CategoryValueStructured categoryValueModel);

}
