package com.silenteight.sens.webapp.users.bulk;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.bulk.dto.*;
import com.silenteight.sens.webapp.users.bulk.dto.BulkCreateUsersRequest.NewUser;
import com.silenteight.sens.webapp.users.user.UserService;
import com.silenteight.sens.webapp.users.user.dto.AddRoleRequest;
import com.silenteight.sens.webapp.users.user.dto.CreateUserRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_ANALYST;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_APPROVER;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_MAKER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BulkUserManagementServiceTest {

  private static final String USER_NAME_1 = "maker";
  private static final String DESCRIPTION_1 = "description_maker";
  private static final String USER_NAME_2 = "approver";
  private static final String DESCRIPTION_2 = "description_approver";
  private static final Role ROLE_1 = ROLE_MAKER;
  private static final Role ROLE_2 = ROLE_APPROVER;
  private static final Role COMMON_ROLE = ROLE_ANALYST;
  private static final long USER_ID_1 = 1;
  private static final long USER_ID_2 = 2;

  @Mock
  private UserService userService;

  private BulkUserManagementService classUnderTest;

  @Before
  public void setUp() {
    classUnderTest = new BulkUserManagementService(userService);
  }

  @Test
  public void forGivenUsersShouldCreateUsers() {
    // given
    BulkCreateUsersRequest request = new BulkCreateUsersRequest(
        asList(makeNewUser(USER_NAME_1, ROLE_1, DESCRIPTION_1),
               makeNewUser(USER_NAME_2, ROLE_2, DESCRIPTION_2)));

    // when
    classUnderTest.bulkCreateUsers(request);

    // then
    verify(userService).create(makeCreateUserRequest(USER_NAME_1, ROLE_1, DESCRIPTION_1));
    verify(userService).create(makeCreateUserRequest(USER_NAME_2, ROLE_2, DESCRIPTION_2));
  }

  @Test
  public void forGivenUserIdsShouldAddRoleToGivenUsers() {
    // given
    BulkAddRoleToUsersRequest request = new BulkAddRoleToUsersRequest(
        asList(USER_ID_1, USER_ID_2), COMMON_ROLE);

    // when
    classUnderTest.bulkAddRoleToUsers(request);

    // then
    verify(userService).addRole(makeAddRoleRequest(USER_ID_1, COMMON_ROLE));
    verify(userService).addRole(makeAddRoleRequest(USER_ID_2, COMMON_ROLE));
  }

  @Test
  public void forGivenUserNamesShouldDeleteGivenUsers() {
    // given
    BulkDeleteUsersRequest request = new BulkDeleteUsersRequest(asList(USER_NAME_1, USER_NAME_2));

    // when
    classUnderTest.bulkDeleteUsers(request);

    // then
    verify(userService).delete(USER_NAME_1);
    verify(userService).delete(USER_NAME_2);
  }

  @Test
  public void forGivenAnalystsSynchronizeAnalysts() {
    // given
    List<Analyst> analysts = asList(
        new Analyst(USER_NAME_1, DESCRIPTION_1),
        new Analyst(USER_NAME_2, DESCRIPTION_2));

    // when
    classUnderTest.synchronizeAnalysts(new BulkCreateAnalystsRequest(analysts));

    // then
    verify(userService).findAllOrderByUserName();
  }

  private NewUser makeNewUser(String name, Role role, String description) {
    return new NewUser(name, role, description);
  }

  private CreateUserRequest makeCreateUserRequest(String name, Role role, String displayName) {
    return CreateUserRequest
        .builder()
        .name(name)
        .displayName(displayName)
        .superUser(false)
        .roles(singletonList(role))
        .build();
  }

  private AddRoleRequest makeAddRoleRequest(long id, Role role) {
    return new AddRoleRequest(id, role);
  }
}
