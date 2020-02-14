package com.silenteight.sens.webapp.user.sync.analyst;

import com.silenteight.sens.webapp.user.sync.analyst.dto.Analyst;

import java.util.Collection;

interface ExternalAnalystRepository {

  Collection<Analyst> list();
}
