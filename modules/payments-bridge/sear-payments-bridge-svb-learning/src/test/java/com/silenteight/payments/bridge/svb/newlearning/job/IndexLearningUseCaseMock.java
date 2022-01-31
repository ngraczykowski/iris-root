package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlert;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexRegisteredAlert;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import java.util.List;

public class IndexLearningUseCaseMock implements IndexLearningUseCase {

  @Override
  public void indexForLearning(List<IndexRegisteredAlert> alerts) {

  }

  @Override
  public void index(List<IndexAlert> alerts) {

  }
}
