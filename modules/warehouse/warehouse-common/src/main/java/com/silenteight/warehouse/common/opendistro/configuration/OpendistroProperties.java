package com.silenteight.warehouse.common.opendistro.configuration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.opendistro")
public class OpendistroProperties {

  @NotNull
  Integer maxObjectCount;
}
