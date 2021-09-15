package com.silenteight.payments.bridge.svb.learning.categories.port.outgoing;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;

public interface CreateCategoryValuesClient {

  void createCategoriesValues(BatchCreateCategoryValuesRequest createCategoryValuesRequest);
}
