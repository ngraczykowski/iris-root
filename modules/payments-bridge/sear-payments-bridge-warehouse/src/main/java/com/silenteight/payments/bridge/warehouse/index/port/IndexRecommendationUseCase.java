package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.IndexRecommendationRequest;

public interface IndexRecommendationUseCase {

  void index(IndexRecommendationRequest request);

}
