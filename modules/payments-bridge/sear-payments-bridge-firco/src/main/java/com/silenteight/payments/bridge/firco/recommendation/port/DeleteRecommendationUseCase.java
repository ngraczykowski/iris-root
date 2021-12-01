package com.silenteight.payments.bridge.firco.recommendation.port;

import java.util.List;
import java.util.UUID;

public interface DeleteRecommendationUseCase {

  void delete(List<UUID> alertMessageIds);

}
