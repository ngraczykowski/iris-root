package com.silenteight.bridge.core.registration.domain.model;

import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus;

public record BatchPriorityWithStatus(Integer priority, BatchStatus status) {}
