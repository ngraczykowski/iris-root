package com.silenteight.serp.governance.qa.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.analysis.details.AlertDetailsQuery;
import com.silenteight.serp.governance.qa.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.analysis.list.ListAlertQuery;
import com.silenteight.serp.governance.qa.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.common.AlertResource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.qa.common.DecisionStateConverter.asStringList;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.ANALYSIS;

@RequiredArgsConstructor
public class AlertAnalysisQuery implements ListAlertQuery, AlertDetailsQuery {

  private static final Integer ANALYSIS_STATE = ANALYSIS.getValue();

  @NonNull
  AlertRepository alertRepository;
  @NonNull
  DecisionRepository decisionRepository;

  @Override
  public List<AlertAnalysisDto> list(
      List<DecisionState> states, OffsetDateTime createdAt, Integer limit) {

    return decisionRepository.findAllAnalysisByStatesNewerThan(
        ANALYSIS_STATE, asStringList(states), createdAt, limit);
  }

  @Override
  public int count(List<DecisionState> states) {
    return decisionRepository.countAllByLevelAndStates(ANALYSIS_STATE, states);
  }

  @Override
  public AlertAnalysisDetailsDto details(UUID id) {
    return decisionRepository.findAnalysisDetails(AlertResource.toResourceName(id),
        ANALYSIS.getValue());
  }
}
