package com.silenteight.serp.governance.qa.validation.list;

import com.silenteight.serp.governance.qa.domain.DecisionState;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface ListAlertValidationQuery {

  List<AlertValidationDto> list(List<DecisionState> states,
      OffsetDateTime createdAfter,
      Integer limit);

  int count(List<DecisionState> states);
}
