package com.silenteight.sens.webapp.user.sync.analyst;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
class AnalystQuery {

  @NonNull
  private final AnalystQueryRepository repository;

  List<InternalAnalyst> list() {
    log.debug("Listing Analysts");

    return repository.findAll();
  }
}
