/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchSolutionPublisherPort;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

public class MockMatchSolutionPublisherPort implements MatchSolutionPublisherPort {

  @Override
  public void resolve(MatchSolution matchSolution) {

  }
}
