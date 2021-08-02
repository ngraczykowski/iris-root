package com.silenteight.payments.bridge.firco.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertRecommendationDto {

  @JsonProperty("Recommendation")
  private String recommendation;

  @JsonProperty("Comment")
  private String comment;
}
