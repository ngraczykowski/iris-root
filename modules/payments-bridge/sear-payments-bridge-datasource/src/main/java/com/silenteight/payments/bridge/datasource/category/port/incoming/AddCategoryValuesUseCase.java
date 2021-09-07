package com.silenteight.payments.bridge.datasource.category.port.incoming;

import com.silenteight.payments.bridge.datasource.category.model.MatchCategoryValue;

import java.util.List;

public interface AddCategoryValuesUseCase {

  void addCategoryValues(List<MatchCategoryValue> categoryValues);

}
