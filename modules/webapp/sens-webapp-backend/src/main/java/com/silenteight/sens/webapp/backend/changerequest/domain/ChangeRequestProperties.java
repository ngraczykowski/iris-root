package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "sens.webapp.change-request")
@RequiredArgsConstructor
@ConstructorBinding
@Getter
public class ChangeRequestProperties {

  @NotNull
  private final Integer maxClosed;
}
