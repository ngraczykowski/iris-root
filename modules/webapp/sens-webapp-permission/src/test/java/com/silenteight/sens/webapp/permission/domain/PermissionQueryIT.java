package com.silenteight.sens.webapp.permission.domain;

import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.sens.webapp.permission.PermissionTestFixtures.PERMISSION_DESCRIPTION_1;
import static com.silenteight.sens.webapp.permission.PermissionTestFixtures.PERMISSION_ID_1;
import static com.silenteight.sens.webapp.permission.PermissionTestFixtures.PERMISSION_NAME_1;
import static com.silenteight.sens.webapp.permission.PermissionTestFixtures.USERNAME;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PermissionDomainTestConfiguration.class })
class PermissionQueryIT extends BaseDataJpaTest {

  @Autowired
  private PermissionQuery underTest;

  @Test
  void shouldListAllPermissions() {
    // given
    persistPermission(PERMISSION_ID_1);

    // when
    Collection<PermissionDto> result = underTest.listAll();

    // then
    assertThat(result).hasSize(1);
    PermissionDto permission = result.iterator().next();
    assertThat(permission.getId()).isEqualTo(PERMISSION_ID_1);
    assertThat(permission.getName()).isEqualTo(PERMISSION_NAME_1);
    assertThat(permission.getDescription()).isEqualTo(PERMISSION_DESCRIPTION_1);
  }

  private void persistPermission(UUID permissionId) {
    Permission permission = Permission.builder()
        .permissionId(permissionId)
        .name(PERMISSION_NAME_1)
        .description(PERMISSION_DESCRIPTION_1)
        .createdBy(USERNAME)
        .updatedBy(USERNAME)
        .endpointIds(emptySet())
        .build();

    entityManager.persistAndFlush(permission);
  }
}
