package com.silenteight.serp.governance.qa.manage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.validation.details.AlertDetailsQuery;
import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.manage.validation.list.ListAlertValidationQuery;
import com.silenteight.serp.governance.qa.manage.validation.list.dto.AlertValidationDto;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.serp.governance.qa.manage.common.DecisionStateConverter.asStringList;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;

@RequiredArgsConstructor
@Slf4j
class AlertValidationQuery implements AlertDetailsQuery, ListAlertValidationQuery {

  private static final Integer VALIDATION_STATE = VALIDATION.getValue();

  @NonNull
  private final DecisionRepository decisionRepository;

  @Override
  public List<AlertValidationDto> list(
      List<DecisionState> states, OffsetDateTime createdAfter, Integer limit) {
    log.info(
        "Listing QA alerts (states={}, createdAfter={}, limit={})", states, createdAfter, limit);

    return decisionRepository.findAllValidationByStatesNewerThan(
        VALIDATION_STATE, asStringList(states), createdAfter, limit);
  }

  @Override
  public AlertValidationDetailsDto details(String alertName) {
    return decisionRepository.findValidationDetails(alertName, VALIDATION_STATE);
  }

  @Override
  public int count(List<DecisionState> states) {
    return decisionRepository.countAllByLevelAndStates(VALIDATION_STATE, states);
  }
}
