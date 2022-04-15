package com.silenteight.sens.webapp.scb.user.sync.analyst;

import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.Analyst;

import java.util.Collection;

interface ExternalAnalystRepository {

  Collection<Analyst> list();
}
