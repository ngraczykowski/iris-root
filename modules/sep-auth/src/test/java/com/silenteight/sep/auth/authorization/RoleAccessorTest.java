package com.silenteight.sep.auth.authorization;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@WebAppConfiguration
@ContextConfiguration(classes = RoleAccessorConfiguration.class)
@ExtendWith(SpringExtension.class)
class RoleAccessorTest {

  @Autowired
  private RoleAccessor roleAccessor;

  @Test
  void shouldThrowsException() {
    Throwable thrown = catchThrowable(() -> {
      roleAccessor.getRoles();
    });
    assertThat(thrown).isInstanceOf(AccessDeniedException.class);
    assertThat(thrown.getMessage()).isEqualTo("There is no authenticated principal");
  }

  @Test
  @WithMockUser(roles = {"role1", "role2"})
  void shouldCaptureUserRoles() {
    Set<String> authorizationToken = roleAccessor.getRoles();
    assertThat(authorizationToken).contains("ROLE_role1", "ROLE_role2");
  }

  @Test
  @WithMockUser(roles = {})
  void shouldCaptureUserRolesWhenEmpty() {
    Set<String> authorizationToken = roleAccessor.getRoles();
    assertThat(authorizationToken).isEmpty();
  }
}
