package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonCreator;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Data
@ValidRecommendationRequest
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class RecommendationRequest {

  private final String systemId;
  private final String unit;
  private final String account;
  private final String recordDetails;

  boolean hasSystemId() {
    return isNotEmpty(systemId);
  }

  boolean hasAllDetails() {
    return isNotEmpty(unit) && isNotEmpty(account) && isNotEmpty(recordDetails);
  }
}
