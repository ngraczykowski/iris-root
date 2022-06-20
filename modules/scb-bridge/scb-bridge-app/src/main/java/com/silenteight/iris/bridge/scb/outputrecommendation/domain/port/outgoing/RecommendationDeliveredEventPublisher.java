/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.port.outgoing;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.RecommendationsDeliveredEvent;

public interface RecommendationDeliveredEventPublisher {

  void publish(RecommendationsDeliveredEvent event);
}
