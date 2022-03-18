package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;

import java.util.List;

public interface CategoryValueRepository {

  void save(List<CategoryValue> categoryValues);
}
