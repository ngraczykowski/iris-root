package com.silenteight.sens.webapp.sso.details;

import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;

import java.util.UUID;

public interface SsoMappingDetailsQuery {

  SsoMappingDto details(UUID ssoMappingId);
}
