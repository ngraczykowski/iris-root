package com.silenteight.bridge.core.reports.domain.commands;

public record SendReportsCommand(
    String batchId,
    String analysisName
) {}
