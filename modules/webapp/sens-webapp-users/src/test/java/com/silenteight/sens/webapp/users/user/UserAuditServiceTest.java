package com.silenteight.sens.webapp.users.user;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.dto.UserAuditDto;

import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.silenteight.sens.webapp.users.user.UserAuditService.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuditServiceTest {

  private static final String USER_AUDIT_REPORT_HEADER = String.join(
      ",", USER_ID_HEADER, USER_NAME_HEADER, DISPLAY_NAME_HEADER, PASSWORD_CHANGED_HEADER,
      SUPER_USER_HEADER, ACTIVE_HEADER, ROLES_HEADER, AUDITED_OPERATION_HEADER, AUDITED_AT_HEADER,
      MODIFIED_BY_HEADER);

  private static final Instant AUDIT_DATE = Instant.parse("2007-11-03T10:15:30.00Z");

  @Mock
  private UserFinder userFind;

  private UserAuditService underTest;

  @BeforeEach
  void setUp() {
    underTest = new UserAuditService(userFind);
  }

  @Test
  void userAuditReportTest() {
    //given
    List<UserAuditDto> userAuditDto = getUserAuditDtos();
    when(userFind.findAudited()).thenReturn(userAuditDto);

    //when
    List<String> lines = underTest.generateAuditReport().buildLines().collect(toList());

    //then
    assertThat(lines).hasSize(3);
    assertThat(lines.get(0).trim()).isEqualTo(USER_AUDIT_REPORT_HEADER);
    assertThat(lines.get(1).trim()).isEqualTo(
        "1,first,firstDisplay,0,1,1,,INSERT,2007-11-03T10:15:30Z,somebody");
    assertThat(lines.get(2).trim()).isEqualTo(
        "2,second,secondDisplay,1,0,0,ROLE_APPROVER,UPDATE,2007-11-03T10:15:30Z,nobody");
  }

  private List<UserAuditDto> getUserAuditDtos() {
    UserAuditDto first = getUserAuditDto(1L, "first", "firstDisplay", false, true, true,
                                         emptyList(), RevisionType.ADD, "somebody");
    UserAuditDto second = getUserAuditDto(2L, "second", "secondDisplay", true, false, false,
                                          singletonList(Role.ROLE_APPROVER), RevisionType.MOD,
                                          "nobody");
    return asList(first, second);
  }

  private UserAuditDto getUserAuditDto(
      long userId,
      String userName,
      String displayName, boolean passwordChanged,
      boolean superUser,
      boolean active,
      List<Role> roles,
      RevisionType type,
      String modifiedBy) {

    return UserAuditDto
        .builder()
        .userId(userId)
        .userName(userName)
        .displayName(displayName)
        .passwordChanged(passwordChanged)
        .superUser(superUser)
        .active(active)
        .roles(roles)
        .revisionType(type)
        .auditedAt(AUDIT_DATE)
        .modifiedBy(modifiedBy)
        .build();
  }
}
