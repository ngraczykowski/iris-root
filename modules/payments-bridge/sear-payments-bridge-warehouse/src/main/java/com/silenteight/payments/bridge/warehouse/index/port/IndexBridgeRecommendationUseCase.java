package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.common.model.AlertData;

public interface IndexBridgeRecommendationUseCase {

  void index(AlertData alertData, String status, String reason);

}
