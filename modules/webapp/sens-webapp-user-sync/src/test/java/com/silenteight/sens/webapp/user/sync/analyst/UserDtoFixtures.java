package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.dto.UserDto;

import static com.silenteight.sens.webapp.user.domain.UserOrigin.GNS;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.time.OffsetDateTime.parse;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class UserDtoFixtures {

  static final UserDto SENS_USER =
      UserDto
          .builder()
          .userName("jsmith")
          .displayName("John Smith")
          .roles(singletonList("Analyst"))
          .origin(SENS)
          .build();

  static final UserDto GNS_USER_WITHOUT_ANALYST_ROLE =
      UserDto
          .builder()
          .userName("4783589366")
          .roles(singletonList("Maker"))
          .origin(GNS)
          .build();

  static final UserDto GNS_USER_WITHOUT_DISPLAY_NAME =
      UserDto
          .builder()
          .userName("1853900357")
          .roles(singletonList("Analyst"))
          .origin(GNS)
          .build();

  static final UserDto GNS_USER =
      UserDto
          .builder()
          .userName("1122334455")
          .displayName("00001111")
          .roles(singletonList("Analyst"))
          .origin(GNS)
          .build();

  static final UserDto DELETED_GNS_USER =
      UserDto
          .builder()
          .userName("2233445566")
          .displayName("11112222")
          .roles(singletonList("Analyst"))
          .origin(GNS)
          .deletedAt(parse("2011-12-03T10:15:30+01:00"))
          .build();
}
