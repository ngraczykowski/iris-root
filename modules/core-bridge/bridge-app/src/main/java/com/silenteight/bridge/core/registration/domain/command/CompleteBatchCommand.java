package com.silenteight.bridge.core.registration.domain.command;

import com.silenteight.bridge.core.registration.domain.model.Batch;

import java.util.List;

public record CompleteBatchCommand(Batch batch, List<String> alertNames) {}
