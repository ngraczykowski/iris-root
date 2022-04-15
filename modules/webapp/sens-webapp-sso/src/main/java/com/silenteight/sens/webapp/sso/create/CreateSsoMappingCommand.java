package com.silenteight.sens.webapp.sso.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;

import java.util.List;

@Value
@Builder
public class CreateSsoMappingCommand {

  @NonNull
  String name;

  @NonNull
  List<AttributeToRoleDto> attributes;

  @NonNull
  List<String> roles;
}
