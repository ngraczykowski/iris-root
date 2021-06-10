package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.Analyst;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AnalystFixtures {

  static final String ROLES_SCOPE = "frontend";

  static final Analyst ANALYST_WITHOUT_DISPLAY_NAME =
      Analyst
          .builder()
          .userName("4783589366")
          .build();

  static final Analyst ANALYST_WITH_DISPLAY_NAME =
      Analyst
          .builder()
          .userName("1853900357")
          .displayName("5050185537")
          .build();

  static final Analyst NEW_ANALYST =
      Analyst
          .builder()
          .userName("8747091287")
          .displayName("7836441098")
          .build();

  static final Analyst RESTORED_ANALYST =
      Analyst
          .builder()
          .userName("2233445566")
          .displayName("11112222")
          .build();
}
