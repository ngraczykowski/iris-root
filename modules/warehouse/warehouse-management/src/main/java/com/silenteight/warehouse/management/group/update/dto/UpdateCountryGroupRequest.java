package com.silenteight.warehouse.management.group.update.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Pattern;

import static com.silenteight.warehouse.common.web.rest.RestValidationConstants.FIELD_REGEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountryGroupRequest {

  @NonNull
  @Pattern(regexp = FIELD_REGEX)
  private String name;
}
