package com.silenteight.sens.webapp.sso.identityproviders.list;

import com.silenteight.sep.usermanagement.api.dto.IdentityProviderDto;

import java.util.Collection;

public interface ListIdentityProvidersQuery {

  Collection<IdentityProviderDto> listAll();
}
