package com.silenteight.sens.webapp.sso.list;

import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;

import java.util.Collection;

public interface ListSsoMappingsQuery {

  Collection<SsoMappingDto> listAll();
}
