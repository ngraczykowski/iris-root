package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;

public interface CreateCategoryValueUnstructured {

  CategoryValue createCategoryValue(DatasourceUnstructuredModel datasourceUnstructuredModel);
}
