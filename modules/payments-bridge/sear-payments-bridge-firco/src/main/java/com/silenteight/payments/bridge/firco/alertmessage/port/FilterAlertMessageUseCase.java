package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertId;

public interface FilterAlertMessageUseCase {

  boolean isResolvedOrOutdated(AlertId alertId);

  boolean hasTooManyHits(AlertId alertId);

}
