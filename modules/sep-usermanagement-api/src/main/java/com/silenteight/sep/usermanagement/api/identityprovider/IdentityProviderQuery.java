package com.silenteight.sep.usermanagement.api.identityprovider;

import com.silenteight.sep.usermanagement.api.identityprovider.dto.IdentityProviderDto;

import java.util.Collection;

public interface IdentityProviderQuery {

  Collection<IdentityProviderDto> listAll();
}
