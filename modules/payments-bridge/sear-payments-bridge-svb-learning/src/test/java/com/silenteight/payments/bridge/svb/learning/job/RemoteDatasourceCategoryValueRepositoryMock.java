package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.category.CategoryValueRepository;

import java.util.List;

class RemoteDatasourceCategoryValueRepositoryMock implements CategoryValueRepository {

  @Override
  public void save(List<CategoryValue> categoryValues) {

  }
}
