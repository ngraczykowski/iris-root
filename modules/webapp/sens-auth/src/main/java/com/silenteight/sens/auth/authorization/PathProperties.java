package com.silenteight.sens.auth.authorization;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ConstructorBinding
@Data
public class PathProperties {

  @NotBlank
  private final String requestPath;
  @NotNull
  private final List<String> roles;
}
