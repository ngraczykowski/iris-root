package com.silenteight.sens.webapp.backend.changerequest.message;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "sens.webapp.messaging.change-request")
@RequiredArgsConstructor
@ConstructorBinding
public class ChangeRequestMessagingProperties {

  @NotBlank
  private final String exchange;
  @Valid
  @NotNull
  private final ChangeRequestMessagingRouteProperties route;
  @Valid
  @NotNull
  private final ChangeRequestMessagingQueueProperties queue;

  public String exchange() {
    return exchange;
  }

  public String routeCreate() {
    return route.getCreate();
  }

  public String routeApprove() {
    return route.getApprove();
  }

  public String routeReject() {
    return route.getReject();
  }

  public String queueCreate() {
    return queue.getCreate();
  }

  public String queueApprove() {
    return queue.getApprove();
  }

  public String queueReject() {
    return queue.getReject();
  }

  public String routeCancel() {
    return route.getCancel();
  }

  public String queueCancel() {
    return queue.getCancel();
  }
}
