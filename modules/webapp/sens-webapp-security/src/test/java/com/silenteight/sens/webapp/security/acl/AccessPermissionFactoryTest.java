package com.silenteight.sens.webapp.security.acl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.acls.model.Permission;

import static com.silenteight.sens.webapp.kernel.security.SensPermission.DECISION_TREE_VIEW;
import static org.assertj.core.api.Assertions.*;

public class AccessPermissionFactoryTest {

  private AccessPermissionFactory permissionFactory;

  @Before
  public void setUp() {
    permissionFactory = new AccessPermissionFactory();
  }

  @Test
  public void givenPermissionName_buildPermission() {
    // given
    String name = DECISION_TREE_VIEW.name();
    Permission expected = new AccessPermission(DECISION_TREE_VIEW);

    // when
    Permission permission = permissionFactory.buildFromName(name);

    // then
    assertThat(permission).isEqualTo(expected);
  }
}
