package com.silenteight.sep.usermanagement.api.role.dto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class CreateRoleCommand {

  @NonNull
  String name;
  @NonNull
  String description;
}
