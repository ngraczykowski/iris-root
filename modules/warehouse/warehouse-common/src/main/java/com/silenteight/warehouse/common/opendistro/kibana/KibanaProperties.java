package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.kibana")
public class KibanaProperties {

  @NotBlank
  String url;
  @NotBlank
  String username;
  @NotBlank
  String password;
  @NotBlank
  String timezone;
}
