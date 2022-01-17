package com.silenteight.sens.webapp.role.domain;

import com.silenteight.sens.webapp.role.details.dto.RoleDetailsDto;
import com.silenteight.sens.webapp.role.domain.exception.RoleNotFoundException;
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
  }

  @Test
  void shouldGetRoleDetails() {
    // given
    persistRole(ROLE_ID_1);

    // when
    RoleDetailsDto details = underTest.details(ROLE_ID_1);

    // then
    assertThat(details.getId()).isEqualTo(ROLE_ID_1);
    assertThat(details.getName()).isEqualTo(ROLE_NAME_1);
    assertThat(details.getDescription()).isEqualTo(ROLE_DESCRIPTION_1);
    assertThat(details.getPermissions()).isEqualTo(PERMISSION_IDS);
    assertThat(details.getCreatedAt()).isNotNull();
    assertThat(details.getCreatedBy()).isEqualTo(USERNAME_1);
    assertThat(details.getUpdatedAt()).isNotNull();
    assertThat(details.getUpdatedBy()).isEqualTo(USERNAME_1);
  }

  @Test
  void shouldThrowIfRoleNotFoundById() {
    assertThatThrownBy(() -> underTest.details(ROLE_ID_1))
        .isInstanceOf(RoleNotFoundException.class)
        .hasMessageContaining("roleId=" + ROLE_ID_1);
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
