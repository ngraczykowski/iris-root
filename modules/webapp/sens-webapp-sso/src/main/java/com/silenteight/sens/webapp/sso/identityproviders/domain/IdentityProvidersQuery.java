package com.silenteight.sens.webapp.sso.identityproviders.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.sso.identityproviders.list.ListIdentityProvidersQuery;
import com.silenteight.sep.usermanagement.api.IdentityProviderRepository;
import com.silenteight.sep.usermanagement.api.dto.IdentityProviderDto;

import java.util.Collection;

@AllArgsConstructor
class IdentityProvidersQuery implements ListIdentityProvidersQuery {

  @NonNull
  private final IdentityProviderRepository identityProviderRepository;

  @Override
  public Collection<IdentityProviderDto> listAll() {
    return identityProviderRepository.findAll();
  }
}
