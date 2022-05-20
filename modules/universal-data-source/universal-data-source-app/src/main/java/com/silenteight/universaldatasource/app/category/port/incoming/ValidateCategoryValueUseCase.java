package com.silenteight.universaldatasource.app.category.port.incoming;

import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;

import java.util.List;

public interface ValidateCategoryValueUseCase {

  void isValid(List<CreateCategoryValuesRequest> categoryValue);

}
