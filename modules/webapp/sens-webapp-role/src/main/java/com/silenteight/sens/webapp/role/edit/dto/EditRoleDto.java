package com.silenteight.sens.webapp.role.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.common.web.rest.RestValidationConstants;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditRoleDto {

  @Nullable
  @Pattern(regexp = RestValidationConstants.FIELD_REGEX)
  String name;
  @Nullable
  @Pattern(regexp = RestValidationConstants.FIELD_REGEX)
  String description;
  @Nullable
  Set<UUID> permissions;
}
