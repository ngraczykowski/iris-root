package com.silenteight.warehouse.common.web.config;

import lombok.Data;

import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.Valid;

@Data
@Validated
public class WebApplicationProperties {

  @Valid
  @NestedConfigurationProperty
  private final CorsConfiguration cors = new CorsConfiguration();
}
