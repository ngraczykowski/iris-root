package com.silenteight.scb.outputrecommendation.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

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

  public boolean isWatchlistLevel() {
    return isNotBlank(hitWatchlistId);
  }
}
