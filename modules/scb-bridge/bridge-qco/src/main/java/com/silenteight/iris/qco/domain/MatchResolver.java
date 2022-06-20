/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import com.silenteight.iris.qco.domain.model.MatchSolution;
import com.silenteight.iris.qco.domain.model.ResolverCommand;

interface MatchResolver {
  MatchSolution overrideSolutionInMatch(ResolverCommand command);
}
