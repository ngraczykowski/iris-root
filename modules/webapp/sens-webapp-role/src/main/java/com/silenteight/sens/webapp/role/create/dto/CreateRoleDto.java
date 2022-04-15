package com.silenteight.sens.webapp.role.create.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.role.domain.DomainConstants.ROLE_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.role.domain.DomainConstants.ROLE_FIELD_MIN_LENGTH;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDto {

  @NonNull
  UUID id;
  @Size(min = ROLE_FIELD_MIN_LENGTH, max = ROLE_FIELD_MAX_LENGTH)
  @NonNull
  String name;
  @Size(min = ROLE_FIELD_MIN_LENGTH, max = ROLE_FIELD_MAX_LENGTH)
  @NonNull
  String description;
  @NonNull
  Set<UUID> permissions;
}
