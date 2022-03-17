package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Data
@ValidRecommendationRequest
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationRequest {

  private String systemId;
  private String unit;
  private String account;
  private String recordDetails;

  boolean hasSystemId() {
    return isNotEmpty(systemId);
  }

  boolean hasAllDetails() {
    return isNotEmpty(unit) && isNotEmpty(account) && isNotEmpty(recordDetails);
  }
}
