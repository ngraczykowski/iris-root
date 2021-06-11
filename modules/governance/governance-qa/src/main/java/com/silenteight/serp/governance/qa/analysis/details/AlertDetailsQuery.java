package com.silenteight.serp.governance.qa.analysis.details;

import com.silenteight.serp.governance.qa.analysis.details.dto.AlertAnalysisDetailsDto;

import java.util.UUID;

public interface AlertDetailsQuery {

  AlertAnalysisDetailsDto details(UUID id);
}
