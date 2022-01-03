package com.silenteight.bridge.core.registration.domain;

import java.util.Set;

public record AddAlertToAnalysisCommand(String batchId, String alertId, Set<String> matchIds) {}
