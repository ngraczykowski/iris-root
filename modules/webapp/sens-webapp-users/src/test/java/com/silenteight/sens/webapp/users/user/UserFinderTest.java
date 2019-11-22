package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.common.query.PageableResult;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.DecisionTreeAssignments.Assignment;
import com.silenteight.sens.webapp.users.user.dto.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFinderTest {

  private static final Role TEST_ROLE = Role.ROLE_MAKER;
  private static final long DECISION_TREE_ID = 1;
  private static final String DECISION_TREE_NAME = "DECISION_TREE_NAME";
  private static final Assignment ASSIGNMENT = new Assignment(
      DECISION_TREE_ID, DECISION_TREE_NAME, TEST_ROLE.getAuthority(), -1);

  @Mock
  private UserService userService;
  @Mock
  private UserAuditDtoMapper userAuditDtoMapper;

  private UserFinder finder;

  @BeforeEach
  void setUp() {
    finder = new UserFinder(
        userService, userAuditDtoMapper, userId -> singletonList(ASSIGNMENT));
  }

  @Nested
  class GivenGetOneContext {

    private static final long USER_1_ID = 1;
    private static final long USER_2_ID = 2;
    private User user1;

    @BeforeEach
    void setUp() {
      user1 = getMockUser(USER_1_ID);
      doReturn(user1).when(userService).getOne(USER_1_ID);
    }

    private User getMockUser(long user1Id) {
      User user = mock(User.class);
      when(user.getId()).thenReturn(user1Id);
      when(user.getUserName()).thenReturn(null);
      when(user.getRoles()).thenReturn(emptyList());
      when(user.isActive()).thenReturn(true);
      when(user.isSuperUser()).thenReturn(true);
      return user;
    }

    @Test
    void shouldReturnCorrectUserViews() {
      User user2 = getMockUser(USER_2_ID);
      doReturn(user2).when(userService).getOne(USER_2_ID);
      assertThat(finder.findUser(USER_1_ID)).extracting(UserView::getId).isEqualTo(USER_1_ID);
      assertThat(finder.findUser(USER_2_ID)).extracting(UserView::getId).isEqualTo(USER_2_ID);
    }

    @Test
    void shouldReturnValidAssignations() {
      assertThat(finder.findUser(USER_1_ID))
          .isEqualTo(new UserDetailedView(user1, singletonList(ASSIGNMENT)));
    }
  }

  @Nested
  class GivenFindContext {

    private static final long USER_1_ID = 1;
    private static final long USER_2_ID = 2;
    private static final int PAGE_SIZE = 10;

    private Pageable pageable = PageRequest.of(0, PAGE_SIZE);

    @BeforeEach
    void setUp() {
      User user1 = mock(User.class);
      when(user1.getId()).thenReturn(USER_1_ID);
      User user2 = mock(User.class);
      when(user2.getId()).thenReturn(USER_2_ID);
      List<User> users = asList(user1, user2);

      doReturn(new PageableResult<>(users, users.size())).when(userService).find(pageable);
    }

    @Test
    void shouldReturnCorrectUserViews() {
      UserResponseView view = finder.findUsers(pageable);

      assertThat(view.getTotal()).isEqualTo(2);
      assertThat(view.getResults()).extracting(UserView::getId)
                                   .containsExactly(USER_1_ID, USER_2_ID);
    }
  }

  @Nested
  class GivenFindAllContext {
    private static final long USER_1_ID = 1;
    private static final long USER_2_ID = 2;

    @BeforeEach
    void setUp() {
      User user1 = mock(User.class);
      when(user1.getId()).thenReturn(USER_1_ID);
      User user2 = mock(User.class);
      when(user2.getId()).thenReturn(USER_2_ID);
      List<User> users = asList(user1, user2);

      doReturn(users).when(userService).findAllOrderByUserName();
    }

    @Test
    void shouldReturnCorrectUserViews() {
      assertThat(finder.findAll()).extracting(UserView::getId)
                                  .containsExactly(USER_1_ID, USER_2_ID);
    }
  }

  @Nested
  class GivenFindAuditedContext {

    @Mock
    UserAudit userAudit1;
    @Mock
    UserAudit userAudit2;
    @Mock
    UserAudit userAudit3;
    @Mock
    UserAuditDto userAuditDto1;
    @Mock
    UserAuditDto userAuditDto2;
    @Mock
    UserAuditDto userAuditDto3;

    @BeforeEach
    void setUp() {
      List<UserAudit> userAudits = asList(userAudit1, userAudit2, userAudit3);

      doReturn(userAudits).when(userService).findAudited();

      doReturn(userAuditDto1).when(userAuditDtoMapper).map(userAudit1);
      doReturn(userAuditDto2).when(userAuditDtoMapper).map(userAudit2);
      doReturn(userAuditDto3).when(userAuditDtoMapper).map(userAudit3);
    }

    @Test
    void shouldReturnCorrectUserAuditDtos() {
      assertThat(finder.findAudited()).containsExactly(
          userAuditDto1, userAuditDto2, userAuditDto3);
    }
  }

  @Nested
  class GivenUserRoleContext {
    private static final long USER_1_ID = 1;
    private static final long USER_2_ID = 2;

    @Test
    void shouldReturnUsersWithoutRole() {
      User user1 = getMockUser(USER_1_ID);
      User user2 = getMockUser(USER_2_ID);
      doReturn(asList(user1, user2)).when(userService).findAll(asList(USER_1_ID, USER_2_ID));

      assertThat(finder.findUsersWithoutRole(asList(USER_1_ID, USER_2_ID), TEST_ROLE))
          .extracting(UserNameView::getId).containsExactlyInAnyOrder(USER_1_ID, USER_2_ID);
    }

    @Test
    void shouldReturnNoUsersWithoutRole() {
      doReturn(emptyList()).when(userService).findAll(asList(USER_1_ID, USER_2_ID));

      assertThat(finder.findUsersWithoutRole(asList(USER_1_ID, USER_2_ID), TEST_ROLE)).isEmpty();
    }

    private User getMockUser(long user1Id) {
      User user = mock(User.class);
      when(user.getId()).thenReturn(user1Id);
      when(user.getUserName()).thenReturn(null);
      when(user.getActiveRoles()).thenReturn(emptyList());
      return user;
    }
  }
}
