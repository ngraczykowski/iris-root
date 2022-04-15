package com.silenteight.sens.webapp.sso.identityproviders.domain;


import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderQuery;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.IdentityProviderDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.sens.webapp.sso.identityproviders.IdentityProvidersTestFixtures.IDENTITY_PROVIDER_LIST;
import static com.silenteight.sens.webapp.sso.identityproviders.IdentityProvidersTestFixtures.IDP_ALIAS_1;
import static com.silenteight.sens.webapp.sso.identityproviders.IdentityProvidersTestFixtures.IDP_DISPLAY_NAME_1;
import static com.silenteight.sens.webapp.sso.identityproviders.IdentityProvidersTestFixtures.IDP_INTERNAL_ID_1;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityProvidersQueryTest {

  @InjectMocks
  IdentityProvidersQuery underTest;

  @Mock
  IdentityProviderQuery identityProviderQuery;

  @Test
  void shouldReturnIdentityProvidersDtoList() {
    //given + when
    when(identityProviderQuery.listAll()).thenReturn(IDENTITY_PROVIDER_LIST);

    //then
    List<IdentityProviderDto> identityProviderDtosList = new ArrayList<>(underTest.listAll());
    assertThat(identityProviderDtosList).hasSize(2);
    IdentityProviderDto identityProviderDto = identityProviderDtosList.get(0);
    assertThat(identityProviderDto.getAlias()).isEqualTo(IDP_ALIAS_1);
    assertThat(identityProviderDto.getDisplayName()).isEqualTo(IDP_DISPLAY_NAME_1);
    assertThat(identityProviderDto.getInternalId()).isEqualTo(IDP_INTERNAL_ID_1);
    assertThat(identityProviderDto.isEnabled()).isTrue();
  }
}
