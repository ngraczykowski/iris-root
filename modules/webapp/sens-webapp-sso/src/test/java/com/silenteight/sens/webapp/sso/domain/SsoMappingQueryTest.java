package com.silenteight.sens.webapp.sso.domain;

import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;
import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.sens.webapp.sso.SsoMappingTestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SsoMappingQueryTest {

  @InjectMocks
  SsoMappingsDetailsQuery underTest;

  @Mock
  IdentityProviderRoleMapper identityProviderRoleMapper;

  @Test
  void shouldReturnSsoMappingDtoList() {
    //given
    when(identityProviderRoleMapper.listDefaultIdpMappings()).thenReturn(ROLE_MAPPING_DTO_LIST);

    //when
    List<SsoMappingDto> ssoMappingDtoList = new ArrayList<>(underTest.listAll());

    //then
    assertThat(ssoMappingDtoList).hasSize(3);
    SsoMappingDto ssoMappingDto = ssoMappingDtoList.get(0);
    assertThat(ssoMappingDto.getName()).isEqualTo(SS0_NAME);
    assertThat(ssoMappingDto.getRoles()).contains(ROLE_NAME_1, ROLE_NAME_2);
  }

  @Test
  void shouldReturnSingleSsoMappingDto() {
    //given
    when(identityProviderRoleMapper.getMapping(SSO_ID_1)).thenReturn(ROLE_MAPPING_DTO_1);

    //when
    SsoMappingDto ssoMappingDto = underTest.details(SSO_ID_1);

    //then
    assertThat(ssoMappingDto.getName()).isEqualTo(SS0_NAME);
    assertThat(ssoMappingDto.getRoles()).hasSize(2);
    assertThat(ssoMappingDto.getRoles()).contains(ROLE_NAME_1, ROLE_NAME_2);
  }
}
