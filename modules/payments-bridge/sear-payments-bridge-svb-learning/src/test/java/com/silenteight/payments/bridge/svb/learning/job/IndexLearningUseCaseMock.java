package com.silenteight.payments.bridge.svb.learning.job;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertRequest;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import java.util.List;
import javax.annotation.Nonnull;

@Slf4j
public class IndexLearningUseCaseMock implements IndexLearningUseCase {

  @Override
  public void index(@Nonnull List<IndexAlertRequest> alerts) {
    log.info("Mocked indexing implementation alerts of size:{}", alerts.size());
  }
}
