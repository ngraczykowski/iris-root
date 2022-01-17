package com.silenteight.serp.governance.qa.manage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.analysis.details.AlertDetailsQuery;
import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.manage.analysis.list.ListAlertQuery;
import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.serp.governance.qa.manage.common.DecisionStateConverter.asStringList;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;

@RequiredArgsConstructor
@Slf4j
public class AlertAnalysisQuery implements ListAlertQuery, AlertDetailsQuery {

  private static final Integer ANALYSIS_STATE = ANALYSIS.getValue();

  @NonNull
  AlertRepository alertRepository;
  @NonNull
  DecisionRepository decisionRepository;

  @Override
  public List<AlertAnalysisDto> list(
      List<DecisionState> states, OffsetDateTime createdAfter, Integer limit) {
    log.info(
        "Listing QA alerts (states={}, createdAfter={}, limit={})", states, createdAfter, limit);

    return decisionRepository.findAllAnalysisByStatesNewerThan(
        ANALYSIS_STATE, asStringList(states), createdAfter, limit);
  }

  @Override
  public int count(List<DecisionState> states) {
    return decisionRepository.countAllByLevelAndStates(ANALYSIS_STATE, states);
  }

  @Override
  public AlertAnalysisDetailsDto details(String alertName) {
    return decisionRepository.findAnalysisDetails(alertName, ANALYSIS.getValue());
  }
}
