package com.silenteight.payments.bridge.dto.output;

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
  String recommendation;
  @JsonProperty("Comment")
  String comment;
}
