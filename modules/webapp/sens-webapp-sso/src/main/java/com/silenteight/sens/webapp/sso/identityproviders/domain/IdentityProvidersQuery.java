package com.silenteight.sens.webapp.sso.identityproviders.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.sso.identityproviders.list.ListIdentityProvidersQuery;
import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderQuery;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.IdentityProviderDto;


import java.util.Collection;

@AllArgsConstructor
class IdentityProvidersQuery implements ListIdentityProvidersQuery {

  @NonNull
  private final IdentityProviderQuery identityProviderQuery;

  @Override
  public Collection<IdentityProviderDto> listAll() {
    return identityProviderQuery.listAll();
  }
}
