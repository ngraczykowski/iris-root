package com.silenteight.bridge.core.registration.domain.command;

import java.util.List;

public record GetAlertStatisticsCommand(String batchId, List<String> alertsNames) {}
