package com.silenteight.sens.webapp.role.domain;

import com.silenteight.sens.webapp.role.domain.exception.RoleAlreadyAssignedToUserException;
import com.silenteight.sens.webapp.role.validate.RoleAssignmentValidator;
import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { RoleDomainTestConfiguration.class })
class RoleServiceIT extends BaseDataJpaTest {

  @Autowired
  private RoleService underTest;

  @Autowired
  private RoleRepository repository;

  @MockBean
  private RoleAssignmentValidator roleAssignmentValidator;

  @Test
  void shouldCreateRole() {
    // when
    underTest.create(CREATE_ROLE_REQUEST);

    // then
    Optional<Role> roleOpt = repository.findByRoleId(ROLE_ID_1);
    assertThat(roleOpt).isPresent();
    Role role = roleOpt.get();
    assertThat(role.getName()).isEqualTo(ROLE_NAME_1);
    assertThat(role.getDescription()).isEqualTo(ROLE_DESCRIPTION_1);
    assertThat(role.getPermissionIds()).isEqualTo(PERMISSION_IDS_1);
    assertThat(role.getCreatedAt()).isNotNull();
    assertThat(role.getCreatedBy()).isEqualTo(USERNAME_1);
    assertThat(role.getUpdatedAt()).isNotNull();
    assertThat(role.getUpdatedBy()).isEqualTo(USERNAME_1);
  }

  @Test
  void shouldRemoveRoleWhenNoUserHasGivenRole() {
    // given
    persistRole(ROLE_ID_1);
    when(roleAssignmentValidator.isAssigned(ROLE_NAME_1)).thenReturn(false);

    // when
    underTest.remove(ROLE_ID_1);

    // then
    Optional<Role> roleOpt = repository.findByRoleId(ROLE_ID_1);
    assertThat(roleOpt).isNotPresent();
  }

  @Test
  void shouldThrowIfRemovingRoleAssignedToUser() {
    // given
    persistRole(ROLE_ID_1);
    when(roleAssignmentValidator.isAssigned(ROLE_NAME_1)).thenReturn(true);

    // when
    assertThatThrownBy(() -> underTest.remove(ROLE_ID_1))
        .isInstanceOf(RoleAlreadyAssignedToUserException.class)
        .hasMessageContaining(
            format("There are users with role %s. Role will not be removed.", ROLE_NAME_1));
  }

  @Test
  void shouldUpdateRole() {
    // given
    persistRole(EDIT_ROLE_REQUEST.getId());

    // when
    underTest.update(EDIT_ROLE_REQUEST);

    // then
    Optional<Role> roleOpt = repository.findByRoleId(EDIT_ROLE_REQUEST.getId());
    assertThat(roleOpt).isPresent();
    Role role = roleOpt.get();
    assertThat(role.getName()).isEqualTo(EDIT_ROLE_REQUEST.getName());
    assertThat(role.getDescription()).isEqualTo(EDIT_ROLE_REQUEST.getDescription());
    assertThat(role.getPermissionIds()).isEqualTo(EDIT_ROLE_REQUEST.getPermissions());
    assertThat(role.getCreatedAt()).isNotNull();
    assertThat(role.getCreatedBy()).isEqualTo(USERNAME_1);
    assertThat(role.getUpdatedAt()).isNotNull();
    assertThat(role.getUpdatedBy()).isEqualTo(EDIT_ROLE_REQUEST.getUpdatedBy());
  }

  private void persistRole(UUID roleId) {
    Role role = Role.builder()
        .roleId(roleId)
        .name(ROLE_NAME_1)
        .description(ROLE_DESCRIPTION_1)
        .permissionIds(PERMISSION_IDS_1)
        .createdBy(USERNAME_1)
        .updatedBy(USERNAME_1)
        .build();

    entityManager.persistAndFlush(role);
  }
}
