package com.silenteight.sens.webapp.sso.domain;

import com.silenteight.sens.webapp.sso.delete.DeleteSsoMappingRequest;
import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.SSO_ID_1;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SsoMappingServiceTest {

  @InjectMocks
  SsoMappingService underTest;

  @Mock
  IdentityProviderRoleMapper identityProviderRoleMapper;

  private static final DeleteSsoMappingRequest REQUEST = DeleteSsoMappingRequest.builder()
      .ssoMappingId(SSO_ID_1)
      .build();

  @Test
  void shouldPassSsoMappingId() {
    //given + when
    underTest.deleteSsoMapping(REQUEST);

    //then
    ArgumentCaptor<UUID> captor = ArgumentCaptor.forClass(UUID.class);
    verify(identityProviderRoleMapper, times(1)).deleteMapping(captor.capture());
    UUID id = captor.getValue();
    assertThat(id).isEqualTo(SSO_ID_1);
  }
}
