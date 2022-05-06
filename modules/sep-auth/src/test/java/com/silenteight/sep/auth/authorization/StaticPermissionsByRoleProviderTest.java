package com.silenteight.sep.auth.authorization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StaticPermissionsByRoleProviderTest {

  @Test
  void whenPermissionsByRoleAvailable_providePermissionsByRole() {
    // given
    Map<String, Set<String>> permissionsByRole =
        Map.of(
            "APPROVER",
            of("VIEW_REASONING_BRANCHES", "APPROVE_BULK_CHANGES", "APPROVE_CHANGE_REQUESTS"),
            "ADMINISTRATOR",
            of("MANAGE_USERS"));
    StaticPermissionsByRoleProvider underTest =
        new StaticPermissionsByRoleProvider(permissionsByRole);

    // when
    Map<String, Set<String>> result = underTest.provide();

    // then
    assertThat(result).isEqualTo(permissionsByRole);
  }
}
