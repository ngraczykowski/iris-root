package com.silenteight.bridge.core.registration.domain.command;

public record RegisterBatchCommand(String id, Long alertCount, String batchMetadata) {}
