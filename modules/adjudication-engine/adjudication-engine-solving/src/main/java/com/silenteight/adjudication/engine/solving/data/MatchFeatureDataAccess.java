/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import java.util.Map;
import java.util.Set;

public interface MatchFeatureDataAccess {
  @Deprecated
  Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts);

  AlertAggregate findAnalysisAlertAndAggregate(Long analysis, Long alert);
}
