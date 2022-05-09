package com.silenteight.sep.usermanagement.api.identityprovider.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class IdentityProviderDto {

  @NonNull
  String alias;
  @NonNull
  String displayName;
  @NonNull
  String internalId;
  boolean enabled;
}
