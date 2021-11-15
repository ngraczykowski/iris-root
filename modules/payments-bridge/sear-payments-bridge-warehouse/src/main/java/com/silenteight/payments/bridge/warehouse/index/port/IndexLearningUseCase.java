package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlert;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexRegisteredAlert;

import java.util.List;

public interface IndexLearningUseCase {

  void indexForLearning(List<IndexRegisteredAlert> alerts);

  void index(List<IndexAlert> alerts);

}
