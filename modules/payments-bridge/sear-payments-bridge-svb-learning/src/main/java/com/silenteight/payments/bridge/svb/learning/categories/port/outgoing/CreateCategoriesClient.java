package com.silenteight.payments.bridge.svb.learning.categories.port.outgoing;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;

public interface CreateCategoriesClient {

  void createCategories(BatchCreateCategoriesRequest createCategoriesRequest);
}
