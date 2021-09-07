package com.silenteight.payments.bridge.datasource.category.port.outgoing;

import com.silenteight.datasource.categories.api.v1.CategoryValue;
import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryRequest;
import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryValue;

import java.util.Collection;
import java.util.List;

public interface CategoryDataAccess {

  Collection<CategoryValue> batchGetMatchCategoryValues(
      List<MatchCategoryRequest> matchValuesList);

  void saveAll(List<MatchCategoryValue> categoryValues);
}
