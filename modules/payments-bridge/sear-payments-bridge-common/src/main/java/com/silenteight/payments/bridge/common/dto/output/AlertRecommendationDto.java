package com.silenteight.payments.bridge.common.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(UpperCamelCaseStrategy.class)
public class AlertRecommendationDto {

  private String recommendation;

  private String comment;
}
