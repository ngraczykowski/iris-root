package com.silenteight.sens.webapp.sso.create.dto;

import lombok.*;

import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateSsoMappingDto {

  @NonNull
  private String name;

  @NonNull
  private List<AttributeToRoleDto> attributes;

  @NonNull
  private List<String> roles;
}
