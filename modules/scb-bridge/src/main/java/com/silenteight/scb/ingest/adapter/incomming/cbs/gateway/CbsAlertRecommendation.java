package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Value
@Builder
class CbsAlertRecommendation {

  @NonNull 
  String alertExternalId;
  @NonNull 
  String batchId;
  String hitWatchlistId;
  String hitRecommendedStatus;
  String hitRecommendedComments;
  String listRecommendedStatus;
  String listRecommendedComments;

  boolean isWatchlistLevel() {
    return isNotBlank(hitWatchlistId);
  }
}
