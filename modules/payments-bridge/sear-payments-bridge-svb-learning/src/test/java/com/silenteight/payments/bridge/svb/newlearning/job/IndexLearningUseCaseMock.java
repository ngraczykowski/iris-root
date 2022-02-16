package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertRequest;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import java.util.List;
import javax.annotation.Nonnull;

public class IndexLearningUseCaseMock implements IndexLearningUseCase {

  @Override
  public void index(@Nonnull List<IndexAlertRequest> alerts) {

  }
}
