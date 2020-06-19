package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@ConstructorBinding
@Data
public class CircuitBreakerDiscrepancyMessagingRouteProperties {

  @NotBlank
  private final String archive;
}
