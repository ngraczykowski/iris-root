package com.silenteight.simulator.processing.alert.index.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.simulator.processing.alert.index.domain.exception.IndexedAlertEntityNotFoundException;
import com.silenteight.simulator.processing.alert.index.dto.IndexedAlertDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class IndexedAlertQuery {

  @NonNull
  private final IndexedAlertRepository repository;

  public List<IndexedAlertDto> findAllByAnalysisName(@NonNull String analysisName) {
    return repository
        .findAllByAnalysisName(analysisName)
        .stream()
        .map(IndexedAlertQuery::toDto)
        .collect(toList());
  }

  public String getAnalysisNameByRequestId(@NonNull String requestId) {
    return repository
        .findAnalysisNameUsingRequestId(requestId)
        .orElseThrow(() -> new IndexedAlertEntityNotFoundException(requestId));
  }

  public IndexedAlertEntity getIndexedAlertEntityByRequestId(@NonNull String requestId) {
    return repository
        .findByRequestId(requestId)
        .orElseThrow(() -> new IndexedAlertEntityNotFoundException(requestId));
  }

  public long count(@NonNull String analysisName, @NonNull List<State> states) {
    return repository.countAllByAnalysisNameAndStateIn(analysisName, states);
  }

  public long sumAllAlertsCountWithAnalysisName(@NonNull String analysisName) {
    return repository.sumAllAlertsCountWithAnalysisName(analysisName);
  }

  private static IndexedAlertDto toDto(@NonNull IndexedAlertEntity entity) {
    return IndexedAlertDto.builder()
        .analysisName(entity.getAnalysisName())
        .alertCount(entity.getAlertCount())
        .state(entity.getState())
        .requestId(entity.getRequestId())
        .build();
  }
}
