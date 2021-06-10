package com.silenteight.sens.webapp.user.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "sens.webapp.roles")
@RequiredArgsConstructor
@ConstructorBinding
@Getter
public class RolesProperties {

  @NotNull
  private final String rolesScope;

  @NotNull
  private final String countryGroupsScope;
}
