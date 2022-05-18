package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertRequest;

import java.util.List;

public interface IndexLearningUseCase {

  void index(List<IndexAlertRequest> alerts);

}
