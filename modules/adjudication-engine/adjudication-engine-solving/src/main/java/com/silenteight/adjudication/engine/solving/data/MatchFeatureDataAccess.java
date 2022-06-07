/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;

import java.util.Map;
import java.util.Set;

public interface MatchFeatureDataAccess {

  Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts);
}
