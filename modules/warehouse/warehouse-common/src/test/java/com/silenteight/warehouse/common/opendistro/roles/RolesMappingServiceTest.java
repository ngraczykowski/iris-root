package com.silenteight.warehouse.common.opendistro.roles;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.RoleMappingDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE_STRING;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesMappingServiceTest {

  @Mock
  private OpendistroElasticClient opendistroElasticClient;

  @Captor
  private ArgumentCaptor<RoleMappingDto> roleMappingDtoCaptor;

  @InjectMocks
  private RolesMappingService underTest;

  @Test
  void shouldCallOpendistroClient() {
    underTest.attachBackendRoleToRole(ELASTIC_ALLOWED_ROLE);

    verify(opendistroElasticClient)
        .setRoleMapping(eq(ELASTIC_ALLOWED_ROLE_STRING), roleMappingDtoCaptor.capture());

    assertThat(roleMappingDtoCaptor.getValue().getBackendRoles())
        .containsExactly(ELASTIC_ALLOWED_ROLE_STRING);
  }
}
