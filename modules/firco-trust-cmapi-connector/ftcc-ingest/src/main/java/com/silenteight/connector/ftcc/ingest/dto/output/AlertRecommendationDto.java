package com.silenteight.connector.ftcc.ingest.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
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
