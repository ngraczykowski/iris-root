package com.silenteight.adjudication.engine.solving.application.publisher;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated;

public interface RecommendationPublisher {

  void publish(RecommendationsGenerated recommendationsGenerated);
}
