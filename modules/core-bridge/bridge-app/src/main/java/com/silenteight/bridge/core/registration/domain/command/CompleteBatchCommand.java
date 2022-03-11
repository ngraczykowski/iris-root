package com.silenteight.bridge.core.registration.domain.command;

import java.util.List;

public record CompleteBatchCommand(String id, List<String> alertNames) {}
