package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;

interface CategoryValueProcess {

  CategoryValue extract(CategoryValueExtractModel categoryValueExtractModel);
}
