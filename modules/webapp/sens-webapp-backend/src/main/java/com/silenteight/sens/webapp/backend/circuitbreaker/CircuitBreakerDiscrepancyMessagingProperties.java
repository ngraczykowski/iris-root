package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "sens.webapp.messaging.circuit-breaker.discrepancy")
@RequiredArgsConstructor
@ConstructorBinding
public class CircuitBreakerDiscrepancyMessagingProperties {

  @NotBlank
  private final String exchange;

  @Valid
  @NotNull
  private final CircuitBreakerDiscrepancyMessagingRouteProperties route;

  String exchange() {
    return exchange;
  }

  String routeArchive() {
    return route.getArchive();
  }
}
