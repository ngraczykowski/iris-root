package com.silenteight.bridge.core.registration.domain.command;

import java.util.List;

public record GetAlertsWithoutMatchesCommand(String batchId, List<String> alertsName) {}
