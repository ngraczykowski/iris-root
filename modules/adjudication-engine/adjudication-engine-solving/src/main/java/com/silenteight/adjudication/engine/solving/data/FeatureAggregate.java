/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import java.io.Serializable;

public record FeatureAggregate(long agentConfigFeatureId,
                               String featureName,
                               String agentConfig,
                               String featureValue,
                               String featureReason) implements Serializable {
}
