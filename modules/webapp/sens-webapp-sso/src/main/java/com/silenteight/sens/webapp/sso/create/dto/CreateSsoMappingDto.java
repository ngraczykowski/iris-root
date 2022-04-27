package com.silenteight.sens.webapp.sso.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MIN_LENGTH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSsoMappingDto {

  @NotNull
  @Size(min = SSO_MAPPING_FIELD_MIN_LENGTH, max = SSO_MAPPING_FIELD_MAX_LENGTH)
  private String name;

  @NotNull
  @Valid
  private List<AttributeToRoleDto> attributes;

  @NotNull
  private List<String> roles;
}
