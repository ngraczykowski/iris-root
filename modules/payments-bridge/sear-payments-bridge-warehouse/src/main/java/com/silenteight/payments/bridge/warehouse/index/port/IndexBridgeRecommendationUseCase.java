package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.IndexBridgeRecommendationRequest;

public interface IndexBridgeRecommendationUseCase {

  void index(IndexBridgeRecommendationRequest request);

}
