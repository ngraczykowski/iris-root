package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.payments.bridge.common.model.AlertData;

public interface IndexRecommendationUseCase {

  void index(AlertData alertData, RecommendationWithMetadata recommendationWithMetadata);

}
