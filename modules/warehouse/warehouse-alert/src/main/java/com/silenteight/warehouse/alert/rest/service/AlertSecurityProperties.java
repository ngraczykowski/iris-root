package com.silenteight.warehouse.alert.rest.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "warehouse.alertlevelsecurity")
public class AlertSecurityProperties {

  private boolean enabled = true;
}
