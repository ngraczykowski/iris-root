package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.InternalAnalyst;

import java.util.List;

interface AnalystQueryRepository {

  List<InternalAnalyst> findAll();
}
