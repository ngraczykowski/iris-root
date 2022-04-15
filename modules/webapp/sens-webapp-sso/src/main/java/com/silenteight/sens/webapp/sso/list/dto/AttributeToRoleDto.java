package com.silenteight.sens.webapp.sso.list.dto;

import lombok.*;

import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MIN_LENGTH;

@Builder
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class AttributeToRoleDto {

  @NonNull
  @Size(min = SSO_MAPPING_FIELD_MIN_LENGTH, max = SSO_MAPPING_FIELD_MAX_LENGTH)
  String attribute;

  @NonNull
  @Size(min = SSO_MAPPING_FIELD_MIN_LENGTH, max = SSO_MAPPING_FIELD_MAX_LENGTH)
  String role;
}
