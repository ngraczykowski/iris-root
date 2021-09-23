package com.silenteight.universaldatasource.app.category.port.incoming;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;

import java.util.List;

public interface CreateCategoryValuesUseCase {

  BatchCreateCategoryValuesResponse addCategoryValues(
      List<CreateCategoryValuesRequest> categoryValues);

}
