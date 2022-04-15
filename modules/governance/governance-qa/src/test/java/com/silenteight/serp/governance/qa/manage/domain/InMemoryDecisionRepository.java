package com.silenteight.serp.governance.qa.manage.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;
import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.manage.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertIdException;
import com.silenteight.serp.governance.qa.manage.domain.exception.WrongAlertNameException;
import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.manage.validation.list.dto.AlertValidationDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class InMemoryDecisionRepository
    extends BasicInMemoryRepository<Decision>
    implements DecisionRepository {

  private final InMemoryAlertRepository alertRepository;

  public InMemoryDecisionRepository(InMemoryAlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  @Override
  public Optional<Decision> findByAlertNameAndLevel(String alertName, Integer level) {
    return stream().filter(decision -> level.equals(decision.getLevel()))
        .findFirst();
  }

  @Override
  public int countAllByLevelAndStates(Integer level, List<DecisionState> states) {
    return (int) stream()
        .filter(decision -> states.contains(decision.getState())
            && decision.getLevel().equals(level))
        .count();
  }

  @Override
  public AlertAnalysisDetailsDto findAnalysisDetails(String alertName, Integer level) {
    Alert alert = alertRepository.findByAlertName(alertName)
        .orElseThrow(() -> new WrongAlertNameException(alertName));
    Decision decision = findDecisionByAlertNameAndLevel(alert, level);

    return DummyAlertAnalysisDetailsDto.builder()
        .alertName(alert.getAlertName())
        .state(decision.getState())
        .decisionComment(decision.getComment())
        .decisionBy(decision.getDecidedBy())
        .decisionAt(decision.getDecidedAt())
        .addedAt(alert.getCreatedAt())
        .build();
  }

  private Decision findDecisionByAlertNameAndLevel(Alert alert, Integer level) {
    return stream().filter(current ->
        current.getAlertId().equals(alert.getId()) && current.getLevel().equals(level))
        .findFirst().orElseThrow(() -> new WrongAlertNameException(alert.getAlertName()));
  }

  @Override
  public List<AlertAnalysisDto> findAllAnalysisByStatesNewerThan(
      Integer level, List<String> states, OffsetDateTime createdAt, Integer limit) {

    return findAllByLevelAndStatesNewerThan(level, states, createdAt, limit)
        .map(decision -> {
          Alert alert = alertRepository.findById(decision.getAlertId())
              .orElseThrow(() -> new WrongAlertIdException(decision.getAlertId()));

          return AlertAnalysisDtoBuilder.builder()
              .alertName(alert.getAlertName())
              .addedAt(toInstant(alert.getCreatedAt()))
              .state(decision.getState())
              .decisionAt(toInstant(decision.getDecidedAt()))
              .decisionBy(decision.getDecidedBy())
              .decisionComment(decision.getComment())
              .build();
        }).collect(toList());
  }

  private Stream<Decision> findAllByLevelAndStatesNewerThan(Integer level, List<String> states,
      OffsetDateTime createdAt, Integer limit) {
    List<Alert> alertStream = alertRepository.findNewerThan(createdAt, limit);
    return stream()
        .filter(decision ->
            states.contains(decision.getState().toString())
            && decision.getLevel().equals(level)
            && alertStream.stream().anyMatch(alert -> alert.getId().equals(decision.getAlertId())));
  }

  @Override
  public List<AlertValidationDto> findAllValidationByStatesNewerThan(
      Integer level, List<String> states, OffsetDateTime createdAt, Integer limit) {
    return findAllByLevelAndStatesNewerThan(level, states, createdAt, limit)
        .map(decision -> {
          Alert alert = alertRepository.findById(decision.getAlertId())
              .orElseThrow(() -> new WrongAlertIdException(decision.getAlertId()));

          return AlertValidationDtoBuilder.builder()
              .alertName(alert.getAlertName())
              .addedAt(toInstant(alert.getCreatedAt()))
              .state(decision.getState())
              .decisionAt(toInstant(decision.getDecidedAt()))
              .decisionBy(decision.getDecidedBy())
              .decisionComment(decision.getComment())
              .build();
        }).collect(toList());
  }

  private Instant toInstant(OffsetDateTime offsetDateTime) {
    return offsetDateTime != null ? offsetDateTime.toInstant() : null;
  }

  @Override
  public AlertValidationDetailsDto findValidationDetails(String alertName, Integer level) {
    Alert alert = alertRepository.findByAlertName(alertName)
        .orElseThrow(() -> new WrongAlertNameException(alertName));
    Decision decision = findDecisionByAlertNameAndLevel(alert, level);

    return AlertValidationDetailsDtoBuilder.builder()
        .alertName(alert.getAlertName())
        .state(decision.getState())
        .decisionComment(decision.getComment())
        .decisionBy(decision.getDecidedBy())
        .decisionAt(decision.getDecidedAt())
        .addedAt(alert.getCreatedAt())
        .build();
  }

  @Override
  public List<Decision> findAllByStateAndUpdatedAtOlderThan(
      String state, OffsetDateTime olderThan, Integer limit) {

    return stream()
        .filter(decision -> decision.hasState(state) && decision.hasUpdatedAtBefore(olderThan))
        .collect(toList());
  }

  @Override
  public Decision getById(Long id) {
    return stream().filter(decision -> decision.hasId(id)).findAny().orElse(null);
  }

  @Override
  public void eraseCommentByAlertId(Long alertId, OffsetDateTime updatedAt) {
    stream()
        .filter(decision -> decision.getAlertId().equals(alertId))
        .forEach(decision -> {
          decision.eraseComment();
          decision.setUpdatedAt(updatedAt);
        });
  }
}
