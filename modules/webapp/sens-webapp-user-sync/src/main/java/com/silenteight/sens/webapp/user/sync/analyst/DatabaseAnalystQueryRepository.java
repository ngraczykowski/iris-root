package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import java.util.List;

import static java.util.Collections.emptyList;

class DatabaseAnalystQueryRepository implements AnalystQueryRepository {

  @Override
  public List<InternalAnalyst> findAll() {
    // WA-246(mmastylo) use Keycloak to get analysts
    return emptyList();
  }
}
