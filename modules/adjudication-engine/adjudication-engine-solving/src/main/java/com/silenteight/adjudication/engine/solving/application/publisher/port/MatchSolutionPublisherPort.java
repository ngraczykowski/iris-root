/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher.port;

import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

public interface MatchSolutionPublisherPort {

  void resolve(MatchSolution matchSolution);
}
