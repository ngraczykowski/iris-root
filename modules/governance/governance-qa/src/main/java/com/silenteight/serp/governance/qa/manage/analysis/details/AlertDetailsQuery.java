package com.silenteight.serp.governance.qa.manage.analysis.details;

import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;

public interface AlertDetailsQuery {

  AlertAnalysisDetailsDto details(String alertName);
}
