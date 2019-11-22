package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.common.adapter.audit.AuditService;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.dto.UserType;
import com.silenteight.sens.webapp.users.user.dto.UserView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.silenteight.sens.webapp.users.user.UserAuditService.*;
import static com.silenteight.sens.webapp.users.user.dto.UserType.EXTERNAL;
import static com.silenteight.sens.webapp.users.user.dto.UserType.INTERNAL;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReportServiceTest {

  private static final String USER_VIEW_REPORT_HEADER = String.join(
      ",", USER_ID_HEADER, USER_NAME_HEADER, DISPLAY_NAME_HEADER, USER_TYPE_HEADER,
      SUPER_USER_HEADER, ACTIVE_HEADER, ROLES_HEADER, LAST_LOGIN_AT_HEADER, CREATED_AT_HEADER);

  private static final Instant EARLIER = Instant.parse("2007-11-03T10:15:30.00Z");
  private static final Instant LATER = Instant.parse("2007-12-03T10:15:30.00Z");

  @Mock
  private UserFinder userFind;

  private AuditService<UserView> underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserReportService(userFind);
  }

  @Test
  void userViewReportTest() {
    //given
    List<UserView> userViews = getUserViews();
    when(userFind.findAll()).thenReturn(userViews);

    //when
    List<String> lines = underTest.generateAuditReport().buildLines().collect(toList());

    //then
    assertThat(lines).hasSize(3);
    assertThat(lines.get(0).trim()).isEqualTo(USER_VIEW_REPORT_HEADER);
    assertThat(lines.get(1).trim()).isEqualTo(
        "1,first,firstDisplay,EXTERNAL,1,1,,2007-12-03T10:15:30Z,2007-11-03T10:15:30Z");
    assertThat(lines.get(2).trim()).isEqualTo(
        "2,second,secondDisplay,INTERNAL,0,0,ROLE_APPROVER,2007-12-03T10:15:30Z,"
            + "2007-11-03T10:15:30Z");
  }

  private List<UserView> getUserViews() {
    return asList(
        getUserView(1L, "first", "firstDisplay", true, true, EXTERNAL, emptyList()),
        getUserView(2L, "second", "secondDisplay", false, false, INTERNAL,
                    singletonList(Role.ROLE_APPROVER)));
  }

  private UserView getUserView(
      long id,
      String userName,
      String displayName,
      boolean superuser,
      boolean active,
      UserType userType,
      List<Role> roles) {

    UserView userView = mock(UserView.class);
    when(userView.getId()).thenReturn(id);
    when(userView.getUserName()).thenReturn(userName);
    when(userView.getDisplayName()).thenReturn(displayName);
    when(userView.getType()).thenReturn(userType);
    when(userView.getRoles()).thenReturn(roles);
    when(userView.getRoleNames()).thenCallRealMethod();
    when(userView.getCreatedAt()).thenReturn(EARLIER);
    when(userView.getLastLoginAt()).thenReturn(LATER);
    when(userView.isActive()).thenReturn(active);
    when(userView.isSuperUser()).thenReturn(superuser);
    return userView;
  }
}
