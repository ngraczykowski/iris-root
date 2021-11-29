package com.silenteight.payments.bridge.svb.learning.categories.port.incoming;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import java.util.List;

public interface CreateCategoryValuesUseCase {

  void createCategoryValues(List<LearningAlert> learningAlerts, List<ReadAlertError> errors);
}
