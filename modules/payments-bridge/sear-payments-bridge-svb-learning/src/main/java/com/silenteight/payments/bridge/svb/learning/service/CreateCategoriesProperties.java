package com.silenteight.payments.bridge.svb.learning.service;

import lombok.Data;
import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@ConfigurationProperties("pb.categories")
@Value
@Validated
class CreateCategoriesProperties {

  Map<String, Category> supported;

  @Data
  public static class Category {

    private String displayName;
    private String type;
    private String multiValue;
    private List<String> allowedValues;
  }
}
