package com.silenteight.scb.outputrecommendation.domain.model;

public record RecommendationsDeliveredEvent(
    String batchId,
    String analysisName
) {}
