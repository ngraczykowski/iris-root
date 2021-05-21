package com.silenteight.warehouse.common.opendistro.tenant;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.tenant")
public class TenantProperties {

  @NotNull
  Integer maxObjectCount;
}
