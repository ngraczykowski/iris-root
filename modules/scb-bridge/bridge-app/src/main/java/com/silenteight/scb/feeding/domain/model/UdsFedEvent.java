package com.silenteight.scb.feeding.domain.model;

import lombok.Builder;

import java.util.List;

public record UdsFedEvent(
    String internalBatchId,
    String alertName,
    AlertErrorDescription errorDescription,
    Status feedingStatus,
    List<FedMatch> fedMatches,
    Integer priority) {

  @Builder
  public UdsFedEvent {}

  public enum Status {
    SUCCESS, FAILURE
  }

  public record FedMatch(
      String matchName) {}
}
