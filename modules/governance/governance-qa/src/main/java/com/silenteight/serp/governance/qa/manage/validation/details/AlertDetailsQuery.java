package com.silenteight.serp.governance.qa.manage.validation.details;

import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;

public interface AlertDetailsQuery {

  AlertValidationDetailsDto details(String alertName);
}
