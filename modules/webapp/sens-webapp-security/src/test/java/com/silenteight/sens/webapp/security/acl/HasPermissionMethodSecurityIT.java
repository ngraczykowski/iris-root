package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.common.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sens.webapp.kernel.domain.DecisionTreeId;
import com.silenteight.sens.webapp.kernel.domain.WorkflowLevel;
import com.silenteight.sens.webapp.kernel.security.SensPermission;
import com.silenteight.sens.webapp.kernel.security.SensUserDetails;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.security.acl.HasPermissionMethodSecurityIT.TestConfiguration;
import com.silenteight.sens.webapp.security.acl.dto.AccessListRequest;
import com.silenteight.sens.webapp.security.acl.dto.UserPermissionRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.kernel.security.SensPermission.DECISION_TREE_CHANGE;
import static com.silenteight.sens.webapp.kernel.security.SensPermission.WORKFLOW_LEVEL_ACCEPT;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_APPROVER;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_MAKER;
import static com.silenteight.sens.webapp.security.acl.HasPermissionMethodSecurityIT.TestConfiguration.SUPER_USER_NAME;
import static com.silenteight.sens.webapp.security.acl.HasPermissionMethodSecurityIT.TestConfiguration.USER_ID;
import static com.silenteight.sens.webapp.security.acl.HasPermissionMethodSecurityIT.TestConfiguration.USER_NAME;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = { PostgresTestInitializer.class }, classes = {
    AclMethodSecurityConfiguration.class,
    AclConfiguration.class,
    DomainObjectIdentityGeneratorConfiguration.class,
    TestConfiguration.class
})
@DataJpaTest
@TestPropertySource("classpath:security-test.properties")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = NONE)
public class HasPermissionMethodSecurityIT {

  private static final long DECISION_TREE_ID = 1L;
  private static final int LEVEL = 5;

  @Autowired
  private ProtectedService protectedService;

  @Autowired
  private AccessManagementService accessManagementService;

  @Test
  @WithUserDetails(USER_NAME)
  public void givenUserAndNoAccessList_throwAccessDeniedException() {
    // given
    DecisionTreeId decisionTreeId = makeDecisionTreeId();
    WorkflowLevel workflowLevel = makeWorkflowLevel();

    // then
    assertThatThrownBy(() -> protectedService.getIdentity(decisionTreeId))
        .isInstanceOf(AccessDeniedException.class);
    assertThatThrownBy(() -> protectedService.getIdentity(workflowLevel))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  @WithUserDetails(USER_NAME)
  public void givenUserAndDecisionTreeIdAccessManagement_controlProtectedMethodAccess() {
    // given
    DecisionTreeId decisionTreeId = makeDecisionTreeId();
    AccessListRequest accessListRequest = new AccessListRequest(decisionTreeId);
    UserPermissionRequest userPermissionRequest = makeUserPermissionRequest(
        decisionTreeId, ROLE_MAKER, asList(DECISION_TREE_CHANGE));

    // when
    accessManagementService.createAccessList(accessListRequest);
    accessManagementService.grantAccess(userPermissionRequest);
    long result = protectedService.getIdentity(decisionTreeId);

    // then
    assertThat(result).isEqualTo(DECISION_TREE_ID);

    // when
    accessManagementService.revokeAccess(userPermissionRequest);

    // then
    assertThatThrownBy(() -> protectedService.getIdentity(decisionTreeId))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  @WithUserDetails(USER_NAME)
  public void givenUserAndWorkflowLevelAccessManagement_controlProtectedMethodAccess() {
    // given
    WorkflowLevel workflowLevel = makeWorkflowLevel();
    AccessListRequest accessListRequest = new AccessListRequest(workflowLevel);
    UserPermissionRequest userPermissionRequest = makeUserPermissionRequest(
        workflowLevel, ROLE_APPROVER, asList(WORKFLOW_LEVEL_ACCEPT));

    // when
    accessManagementService.createAccessList(accessListRequest);
    accessManagementService.grantAccess(userPermissionRequest);
    String result = protectedService.getIdentity(workflowLevel);

    // then
    assertThat(result).isEqualTo(DECISION_TREE_ID + "@" + LEVEL);

    // when
    accessManagementService.revokeAccess(userPermissionRequest);

    // then
    assertThatThrownBy(() -> protectedService.getIdentity(workflowLevel))
        .isInstanceOf(AccessDeniedException.class);
  }

  @Test
  @WithUserDetails(SUPER_USER_NAME)
  public void givenSuperUserAndNoAccessList_invokeProtectedMethods() {
    // given
    DecisionTreeId decisionTreeId = makeDecisionTreeId();
    WorkflowLevel workflowLevel = makeWorkflowLevel();

    // when
    long forDecisionTreeIdResult = protectedService.getIdentity(decisionTreeId);
    String forWorkflowLevelResult = protectedService.getIdentity(workflowLevel);

    // then
    assertThat(forDecisionTreeIdResult).isEqualTo(DECISION_TREE_ID);
    assertThat(forWorkflowLevelResult).isEqualTo(DECISION_TREE_ID + "@" + LEVEL);
  }

  private DecisionTreeId makeDecisionTreeId() {
    return DecisionTreeId.of(DECISION_TREE_ID);
  }

  private WorkflowLevel makeWorkflowLevel() {
    return WorkflowLevel.of(DECISION_TREE_ID, LEVEL);
  }

  private UserPermissionRequest makeUserPermissionRequest(
      Object domainObject, Role role, List<SensPermission> permissions) {

    return new UserPermissionRequest(
        domainObject,
        USER_ID,
        role.getAuthority(),
        new HashSet<>(permissions));
  }

  @Configuration
  @EnableAutoConfiguration
  static class TestConfiguration {

    static final String USER_NAME = "username";
    static final String SUPER_USER_NAME = "superusername";

    static final long USER_ID = 100L;
    static final String USER_PASSWORD = "password";

    private static final Map<String, Boolean> SUPER_USERS = new HashMap<>();

    static {
      SUPER_USERS.put(USER_NAME, false);
      SUPER_USERS.put(SUPER_USER_NAME, true);
    }

    @Bean
    ProtectedService protectedService() {
      return new ProtectedService();
    }

    @Bean
    ConversionService conversionService() {
      return new DefaultConversionService();
    }

    @Bean
    public UserDetailsService userDetailsService() {
      return username -> makeUserDetails(username, SUPER_USERS.get(username));
    }

    private UserDetails makeUserDetails(String username, boolean superUser) {
      return SensUserDetails
          .builder()
          .userId(USER_ID)
          .username(username)
          .authorities(new HashSet<>(asList(ROLE_APPROVER, ROLE_MAKER)))
          .password(USER_PASSWORD)
          .superUser(superUser)
          .build();
    }
  }
}
