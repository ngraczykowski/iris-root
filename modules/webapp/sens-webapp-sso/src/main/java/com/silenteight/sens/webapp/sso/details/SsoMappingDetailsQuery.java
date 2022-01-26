package com.silenteight.sens.webapp.sso.details;

import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;

public interface SsoMappingDetailsQuery {

  SsoMappingDto details(String name);
}
