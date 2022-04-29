package com.silenteight.warehouse.common.properties;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "warehouse.common.recommendation")
@Getter
@Slf4j
public class RecommendationProperties {

  @NonNull
  private Map<String, List<String>> values;

  @NonNull
  private String recommendationFieldName;
}
