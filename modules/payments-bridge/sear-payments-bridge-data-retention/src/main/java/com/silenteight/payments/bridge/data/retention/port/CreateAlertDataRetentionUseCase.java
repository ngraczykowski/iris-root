package com.silenteight.payments.bridge.data.retention.port;

import com.silenteight.payments.bridge.data.retention.model.AlertDataRetention;

public interface CreateAlertDataRetentionUseCase {

  void create(Iterable<AlertDataRetention> alerts);

}
