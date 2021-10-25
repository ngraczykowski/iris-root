package com.silenteight.payments.bridge.svb.learning.categories.port.incoming;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import java.util.List;

public interface CreateCategoryValuesUseCase {

  List<CategoryValue> createCategoryValues(LearningAlert learningAlert);

  void createCategoryValues(List<LearningAlert> learningAlerts, List<ReadAlertError> errors);
}
