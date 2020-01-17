package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.decisiontree.dto.StatusDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class StatusDtoFixtures {

  static final StatusDto INACTIVE = StatusDto
      .builder()
      .name("INACTIVE")
      .build();

  static final StatusDto ACTIVE = StatusDto
      .builder()
      .name("ACTIVE")
      .build();
}
