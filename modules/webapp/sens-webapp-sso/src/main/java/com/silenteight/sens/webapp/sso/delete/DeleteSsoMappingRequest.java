package com.silenteight.sens.webapp.sso.delete;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class DeleteSsoMappingRequest {

  @NonNull
  UUID ssoMappingId;
}
