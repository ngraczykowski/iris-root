package com.silenteight.sens.webapp.role.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.silenteight.sens.webapp.role.RoleTestFixtures.*;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { RoleDomainTestConfiguration.class })
class RoleServiceIT extends BaseDataJpaTest {

  @Autowired
  private RoleService underTest;

  @Autowired
  private RoleRepository repository;

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
    assertThat(role.getPermissionIds()).isEqualTo(PERMISSION_IDS);
    assertThat(role.getCreatedAt()).isNotNull();
    assertThat(role.getCreatedBy()).isEqualTo(USERNAME_1);
    assertThat(role.getUpdatedAt()).isNotNull();
    assertThat(role.getUpdatedBy()).isEqualTo(USERNAME_1);
  }
}
