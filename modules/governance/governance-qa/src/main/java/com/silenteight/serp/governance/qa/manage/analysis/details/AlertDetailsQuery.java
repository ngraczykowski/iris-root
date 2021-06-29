package com.silenteight.serp.governance.qa.manage.analysis.details;

import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;

import java.util.UUID;

public interface AlertDetailsQuery {

  AlertAnalysisDetailsDto details(UUID id);
}
