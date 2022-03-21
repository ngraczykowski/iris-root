package com.silenteight.scb.feeding.domain.model;

import lombok.Builder;

import java.util.List;

public record UdsFedEvent(
    String batchId,
    String alertName,
    AlertErrorDescription errorDescription,
    Status feedingStatus,
    List<FedMatch> fedMatches) {

  @Builder
  public UdsFedEvent {}

  public enum Status {
    SUCCESS, FAILURE
  }

  public record FedMatch(
      String matchName) {}
}
