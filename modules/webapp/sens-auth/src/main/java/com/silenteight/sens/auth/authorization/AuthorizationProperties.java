package com.silenteight.sens.auth.authorization;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "auth")
@RequiredArgsConstructor
@ConstructorBinding
public class AuthorizationProperties {

  @NotNull
  @Valid
  private final List<PathProperties> paths;

  public List<PathProperties> paths() {
    return paths;
  }
}
