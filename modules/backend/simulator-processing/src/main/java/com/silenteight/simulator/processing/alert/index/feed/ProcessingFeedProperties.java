package com.silenteight.simulator.processing.alert.index.feed;

import lombok.Data;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@Validated
@ConfigurationProperties(prefix = "simulator.processing.feed")
public class ProcessingFeedProperties {

  @Valid
  @NestedConfigurationProperty
  private int batchSize;
}
