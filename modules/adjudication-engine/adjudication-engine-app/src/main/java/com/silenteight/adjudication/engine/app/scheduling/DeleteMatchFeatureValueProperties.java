package com.silenteight.adjudication.engine.app.scheduling;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "ae.match-feature-value.not-cached")
class DeleteMatchFeatureValueProperties {

  private List<String> features;
}
