package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.ExternalAnalyst;

import java.util.List;

interface ExternalAnalystRepository {

  List<ExternalAnalyst> list();
}
