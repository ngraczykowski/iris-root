package com.silenteight.bridge.core.registration.domain.command;

public record NotifyBatchErrorCommand(String id, String errorDescription, String batchMetadata) {}
