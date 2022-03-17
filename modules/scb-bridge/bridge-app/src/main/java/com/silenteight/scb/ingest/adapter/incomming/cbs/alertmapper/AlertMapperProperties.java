package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "serp.scb.bridge.cbs.mapper")
@ConstructorBinding
@Value
class AlertMapperProperties {

  static final String DEFAULT_TIME_ZONE = "Asia/Hong_Kong";

  @NotEmpty
  String timeZone = DEFAULT_TIME_ZONE;
}
