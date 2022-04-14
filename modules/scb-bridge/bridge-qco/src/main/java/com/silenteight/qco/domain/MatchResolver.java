package com.silenteight.qco.domain;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.ResolverCommand;

interface MatchResolver {
  MatchSolution overrideSolutionInMatch(ResolverCommand command);
}