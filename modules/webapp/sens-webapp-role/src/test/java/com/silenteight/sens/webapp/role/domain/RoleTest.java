package com.silenteight.sens.webapp.role.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static java.util.Set.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

class RoleTest {

  @Test
  void shouldEdit() {
    // given
    Role role = makeRole();
    String newName = "POLICY_MANAGER";
    String newDescription = "Manager of policies";
    Set<UUID> newPermissions = of(
        fromString("05bf9714-b1ee-4778-a733-6151df70fca3"),
        fromString("2e9f8302-12e3-47c0-ae6c-2c9313785d1d"));

    // when
    role.edit(newName, newDescription, newPermissions, USERNAME_2);

    // then
    assertThat(role.getName()).isEqualTo(newName);
    assertThat(role.getDescription()).isEqualTo(newDescription);
    assertThat(role.getPermissionIds()).isEqualTo(newPermissions);
    assertThat(role.getUpdatedBy()).isEqualTo(USERNAME_2);
  }

  private Role makeRole() {
    return Role.builder()
        .roleId(ROLE_ID_1)
        .name(ROLE_NAME_1)
        .description(ROLE_DESCRIPTION_1)
        .permissionIds(PERMISSION_IDS_1)
        .createdBy(USERNAME_1)
        .updatedBy(USERNAME_1)
        .build();
  }
}
