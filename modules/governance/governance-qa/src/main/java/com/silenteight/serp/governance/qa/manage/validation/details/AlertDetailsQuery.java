package com.silenteight.serp.governance.qa.manage.validation.details;

import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;

import java.util.UUID;

public interface AlertDetailsQuery {

  AlertValidationDetailsDto details(UUID id);
}
