package com.silenteight.scb.outputrecommendation.domain.model;

import java.util.List;

public record RecommendationsDeliveredEvent(
    String batchId,
    String analysisName,
    List<String> alertNames
) {}
