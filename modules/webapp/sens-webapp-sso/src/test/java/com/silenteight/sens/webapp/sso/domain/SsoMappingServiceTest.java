package com.silenteight.sens.webapp.sso.domain;

import com.silenteight.sens.webapp.sso.delete.DeleteSsoMappingRequest;
import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.SS0_NAME;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SsoMappingServiceTest {

  @InjectMocks
  SsoMappingService underTest;

  @Mock
  IdentityProviderRoleMapper identityProviderRoleMapper;

  private static final DeleteSsoMappingRequest REQUEST = DeleteSsoMappingRequest.builder()
      .ssoMappingName(SS0_NAME)
      .build();

  @Test
  void shouldPassSsoMappingName() {
    //given + when
    underTest.deleteSsoMapping(REQUEST);

    //then
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(identityProviderRoleMapper, times(1)).deleteMapping(captor.capture());
    String value = captor.getValue();
    Assertions.assertThat(value).isEqualTo(SS0_NAME);
  }
}
