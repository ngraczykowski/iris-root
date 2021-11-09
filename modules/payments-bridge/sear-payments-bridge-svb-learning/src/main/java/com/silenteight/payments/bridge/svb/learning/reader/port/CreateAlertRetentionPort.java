package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import java.util.List;

public interface CreateAlertRetentionPort {

  void create(List<LearningAlert> learningAlerts);

}
