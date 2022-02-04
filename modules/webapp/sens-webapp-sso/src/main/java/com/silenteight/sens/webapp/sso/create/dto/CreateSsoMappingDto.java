package com.silenteight.sens.webapp.sso.create.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MIN_LENGTH;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateSsoMappingDto {

  @NonNull
  @Size(min = SSO_MAPPING_FIELD_MIN_LENGTH, max = SSO_MAPPING_FIELD_MAX_LENGTH)
  private String name;

  @NonNull
  @Valid
  private List<AttributeToRoleDto> attributes;

  @NonNull
  private List<String> roles;
}
