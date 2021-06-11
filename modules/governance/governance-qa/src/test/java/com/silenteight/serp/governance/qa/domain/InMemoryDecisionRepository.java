package com.silenteight.serp.governance.qa.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;
import com.silenteight.serp.governance.qa.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.domain.exception.WrongAlertNameException;
import com.silenteight.serp.governance.qa.validation.details.dto.AlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public AlertAnalysisDetailsDto findAnalysisDetails(String name, Integer level) {
    Alert alert = alertRepository.findByAlertName(name);
    Decision decision = findDecisionByAlertIdAndLevel(alert, level);

    return DummyAlertAnalysisDetailsDto.builder()
        .alertName(alert.getAlertName())
        .state(decision.getState())
        .decisionComment(decision.getComment())
        .decisionBy(decision.getDecidedBy())
        .decisionAt(decision.getDecidedAt())
        .addedAt(alert.getCreatedAt())
        .build();
  }

  private Decision findDecisionByAlertIdAndLevel(Alert alert, Integer level) {
    return stream().filter(current ->
        current.getAlertId().equals(alert.getId()) && current.getLevel().equals(level))
        .findFirst().orElseThrow(() -> new WrongAlertNameException(alert.getAlertName()));
  }

  @Override
  public List<AlertAnalysisDto> findAllAnalysisByStatesNewerThan(
      Integer level, List<String> states, OffsetDateTime createdAt, Integer limit) {

    return findAllByLevelAndStatesNewerThan(level, states, createdAt, limit)
        .map(decision -> {
          Alert alert = alertRepository.findById(decision.getAlertId());
          return AlertAnalysisDtoBuilder.builder()
              .alertName(alert.getAlertName())
              .addedAt(toInstant(alert.getCreatedAt()))
              .state(decision.getState())
              .decisionAt(toInstant(decision.getDecidedAt()))
              .decisionBy(decision.getDecidedBy())
              .decisionComment(decision.getComment())
              .build();
        }).collect(Collectors.toList());
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
          Alert alert = alertRepository.findById(decision.getAlertId());

          return AlertValidationDtoBuilder.builder()
              .alertName(alert.getAlertName())
              .addedAt(toInstant(alert.getCreatedAt()))
              .state(decision.getState())
              .decisionAt(toInstant(decision.getDecidedAt()))
              .decisionBy(decision.getDecidedBy())
              .decisionComment(decision.getComment())
              .build();
        }).collect(Collectors.toList());
  }

  private Instant toInstant(OffsetDateTime offsetDateTime) {
    return offsetDateTime != null ? offsetDateTime.toInstant() : null;
  }

  @Override
  public AlertValidationDetailsDto findValidationDetails(
      String name, Integer level) {
    Alert alert = alertRepository.findByAlertName(name);
    Decision decision = findDecisionByAlertIdAndLevel(alert, level);

    return AlertValidationDetailsDtoBuilder.builder()
        .alertName(alert.getAlertName())
        .state(decision.getState())
        .decisionComment(decision.getComment())
        .decisionBy(decision.getDecidedBy())
        .decisionAt(decision.getDecidedAt())
        .addedAt(alert.getCreatedAt())
        .build();
  }
}
