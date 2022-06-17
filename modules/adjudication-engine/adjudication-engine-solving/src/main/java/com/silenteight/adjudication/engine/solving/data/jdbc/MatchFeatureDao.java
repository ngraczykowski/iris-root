/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import java.io.Serializable;

record MatchFeatureDao(int priority,String policy,String strategy,long matchId, String clientMatchId,long featureConfigId,
      String featureName, String agentConfig, String featureValue, String featureReason) implements
    Serializable {
}
