package com.silenteight.sens.webapp.role.domain;

import com.silenteight.sens.webapp.role.list.dto.RoleDto;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { RoleDomainTestConfiguration.class })
class RoleQueryIT extends BaseDataJpaTest {

  @Autowired
  private RoleQuery underTest;

  @Test
  void shouldListRoles() {
    // given
    persistRole(ROLE_ID_1);

    // when
    Collection<RoleDto> result = underTest.listAll();

    // then
    assertThat(result).hasSize(1);
    RoleDto role = result.iterator().next();
    assertThat(role.getId()).isEqualTo(ROLE_ID_1);
    assertThat(role.getName()).isEqualTo(ROLE_NAME_1);
    assertThat(role.getDescription()).isEqualTo(ROLE_DESCRIPTION_1);
    assertThat(role.getPermissions()).isEqualTo(PERMISSION_IDS);
    assertThat(role.getCreatedBy()).isEqualTo(USERNAME_1);
    assertThat(role.getUpdatedBy()).isEqualTo(USERNAME_1);
  }

  private void persistRole(UUID roleId) {
    Role role = Role.builder()
        .roleId(roleId)
        .name(ROLE_NAME_1)
        .description(ROLE_DESCRIPTION_1)
        .permissionIds(PERMISSION_IDS)
        .createdBy(USERNAME_1)
        .updatedBy(USERNAME_1)
        .build();

    entityManager.persistAndFlush(role);
  }
}
