package com.silenteight.sep.base.aspects.validation.business.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;

@Value
@Builder
public class BusinessRequest {

  @Max(255)
  @Min(0)
  int integer;

  @Size(max = 8)
  String shortText;

  @NotBlank
  @NotNull
  String requiredText;
}
