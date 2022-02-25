package com.silenteight.bridge.core.recommendation.domain.command;

import java.util.List;

public record ProceedBatchTimeoutCommand(String analysisName, List<String> alertNames) {}
