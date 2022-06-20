/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.port.outgoing;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;

public interface RecommendationPublisher {

  void publishCompleted(RecommendationsGeneratedEvent event);

  void publishError(ErrorRecommendationsGeneratedEvent event);
}
