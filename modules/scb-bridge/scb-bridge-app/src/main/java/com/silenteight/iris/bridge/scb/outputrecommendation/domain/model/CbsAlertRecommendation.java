/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Value
@Builder
public class CbsAlertRecommendation {

  @NonNull
  String alertExternalId;
  @NonNull
  String batchId;
  String hitWatchlistId;
  String hitRecommendedStatus;
  String hitRecommendedComments;
  String listRecommendedStatus;
  String listRecommendedComments;
  QcoInfo qcoInfo;

  public boolean isWatchlistLevel() {
    return isNotBlank(hitWatchlistId);
  }

  @Value
  @Builder
  public static class QcoInfo {

    @NonNull
    String policyId;
    @NonNull
    String hitId;
    @NonNull
    String stepId;
    @NotNull
    String fvSignature;
    @NotNull
    String qcoSampled;
  }
}
