package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.Builder;
import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Map;

@ConstructorBinding
@ConfigurationProperties("feeding")
@Value
@Builder
public class CategoriesConfigurationProperties {

  Map<String, CategoryDefinition> categories;

  @Value
  @Builder
  public static class CategoryDefinition {
    boolean enabled;
    String displayName;
    String categoryType;
    List<String> allowedValues;
    boolean multiValue;
  }
}
