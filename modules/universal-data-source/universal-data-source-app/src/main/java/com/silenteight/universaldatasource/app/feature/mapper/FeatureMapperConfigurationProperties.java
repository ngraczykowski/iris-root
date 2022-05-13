package com.silenteight.universaldatasource.app.feature.mapper;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "uds.feature")
class FeatureMapperConfigurationProperties {

  private Map<String, String> mapping;

  public FeatureMapperConfigurationProperties(Map<String, String> mapping) {
    this.mapping = mapping;
  }
}
