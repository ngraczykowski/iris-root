package com.silenteight.serp.governance.qa.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.validation.details.AlertDetailsQuery;
import com.silenteight.serp.governance.qa.validation.details.dto.AlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.validation.list.ListAlertValidationQuery;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.qa.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.common.DecisionStateConverter.asStringList;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.VALIDATION;

@RequiredArgsConstructor
class AlertValidationQuery implements AlertDetailsQuery, ListAlertValidationQuery {

  private static final Integer VALIDATION_STATE = VALIDATION.getValue();

  @NonNull
  private final AlertRepository alertRepository;
  @NonNull
  private final DecisionRepository decisionRepository;

  @Override
  public List<AlertValidationDto> list(
      List<DecisionState> states, OffsetDateTime createdAfter, Integer limit) {

    return decisionRepository.findAllValidationByStatesNewerThan(
        VALIDATION_STATE, asStringList(states), createdAfter, limit);
  }

  @Override
  public AlertValidationDetailsDto details(UUID id) {
    return decisionRepository.findValidationDetails(toResourceName(id), VALIDATION_STATE);
  }

  @Override
  public int count(List<DecisionState> states) {
    return decisionRepository.countAllByLevelAndStates(VALIDATION_STATE, states);
  }
}
