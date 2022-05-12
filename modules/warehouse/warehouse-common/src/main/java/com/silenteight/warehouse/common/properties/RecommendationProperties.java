package com.silenteight.warehouse.common.properties;

import lombok.*;

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
public class RecommendationProperties {

  @NonNull
  private Map<String, List<String>> values;

  @NonNull
  private String fieldName;
}
