package com.silenteight.bridge.core.registration.domain.command;

import java.util.List;

public record GetBatchWithAlertsCommand(String analysisName, List<String> alertNames) {}
