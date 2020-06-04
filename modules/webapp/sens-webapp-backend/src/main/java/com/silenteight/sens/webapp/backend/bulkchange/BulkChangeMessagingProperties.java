package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "sens.webapp.messaging.bulk-change")
@RequiredArgsConstructor
@ConstructorBinding
class BulkChangeMessagingProperties {

  @NotBlank
  private final String exchange;
  @Valid
  @NotNull
  private final BulkChangeMessagingRouteProperties route;

  String exchange() {
    return exchange;
  }

  String routeCreate() {
    return route.getCreate();
  }

  String routeApply() {
    return route.getApply();
  }

  String routeReject() {
    return route.getReject();
  }
}
