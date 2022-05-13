package com.silenteight.bridge.core.registration.domain.command;

import java.util.Set;

public record GetMatchesWithAlertIdCommand(Set<Long> alertsIds) {}
