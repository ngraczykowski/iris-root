package com.silenteight.serp.governance.qa.manage.analysis.list;

import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;

import java.time.OffsetDateTime;
import java.util.List;

public interface ListAlertQuery {

  List<AlertAnalysisDto> list(
      List<DecisionState> state,
      OffsetDateTime createdAfter,
      Integer limit);

  int count(List<DecisionState> state);
}
