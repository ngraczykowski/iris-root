/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.model;

public record RecommendationsDeliveredEvent(
    String batchId,
    String analysisName
) {}
