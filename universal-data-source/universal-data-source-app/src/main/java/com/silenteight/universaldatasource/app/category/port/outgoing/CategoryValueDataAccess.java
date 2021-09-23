package com.silenteight.universaldatasource.app.category.port.outgoing;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;
import com.silenteight.universaldatasource.app.category.model.MatchCategoryRequest;

import java.util.Collection;
import java.util.List;

public interface CategoryValueDataAccess {

  Collection<CategoryValue> batchGetMatchCategoryValues(
      List<MatchCategoryRequest> matchValuesList);

  List<CreatedCategoryValue> saveAll(List<CreateCategoryValuesRequest> categoryValues);
}
