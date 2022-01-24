package com.silenteight.sens.webapp.sso.delete;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DeleteSsoMappingRequest {

  @NonNull
  String ssoMappingName;
}
