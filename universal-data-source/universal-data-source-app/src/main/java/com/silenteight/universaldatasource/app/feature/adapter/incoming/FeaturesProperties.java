package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "uds.features")
class FeaturesProperties {

  private Map<String, String> mapping = new HashMap<>();

}
