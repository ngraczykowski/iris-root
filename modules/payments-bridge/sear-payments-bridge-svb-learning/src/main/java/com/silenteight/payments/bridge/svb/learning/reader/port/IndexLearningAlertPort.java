package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import java.util.List;

public interface IndexLearningAlertPort {

  void indexForLearning(List<IndexRegisterAlertRequest> indexRegisterAlertRequest);

  void index(List<LearningAlert> learningAlerts);

}
