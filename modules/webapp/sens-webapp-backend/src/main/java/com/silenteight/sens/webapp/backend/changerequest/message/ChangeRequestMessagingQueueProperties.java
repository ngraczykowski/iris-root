package com.silenteight.sens.webapp.backend.changerequest.message;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@ConstructorBinding
@Data
public class ChangeRequestMessagingQueueProperties {

  @NotBlank
  private final String create;
  @NotBlank
  private final String approve;
  @NotBlank
  private final String reject;
  @NotBlank
  private final String cancel;
}
