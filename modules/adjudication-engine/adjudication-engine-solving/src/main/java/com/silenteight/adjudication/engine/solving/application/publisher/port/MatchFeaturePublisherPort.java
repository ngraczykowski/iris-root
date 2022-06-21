/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher.port;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import java.util.List;

public interface MatchFeaturePublisherPort {

  void resolve(AlertSolving alertSolvingModel, Long matchId, List<Feature> features);
}
