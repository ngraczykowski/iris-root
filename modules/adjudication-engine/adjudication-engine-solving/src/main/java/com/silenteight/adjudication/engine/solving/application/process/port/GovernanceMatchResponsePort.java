/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process.port;

import com.silenteight.adjudication.engine.solving.application.process.dto.MatchSolutionResponse;

public interface GovernanceMatchResponsePort {

  void processAlert(final MatchSolutionResponse matchSolutionResponse);
}
