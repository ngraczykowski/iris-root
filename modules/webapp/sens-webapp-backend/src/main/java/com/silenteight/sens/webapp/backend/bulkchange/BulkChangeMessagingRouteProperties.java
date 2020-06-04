package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@ConstructorBinding
@Data
class BulkChangeMessagingRouteProperties {

  @NotBlank
  private final String create;
  @NotBlank
  private final String apply;
  @NotBlank
  private final String reject;
}
