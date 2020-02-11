package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

class AnalystFixtures {

  static final InternalAnalyst INTERNAL_ANALYST_JOHN_SMITH = InternalAnalyst
      .builder()
      .userName("jsmith")
      .displayName("John Smith")
      .build();

  static final InternalAnalyst INTERNAL_ANALYST_ROBERT_DOE = InternalAnalyst
      .builder()
      .userName("rdoe")
      .displayName("Robert Doe")
      .build();
}
