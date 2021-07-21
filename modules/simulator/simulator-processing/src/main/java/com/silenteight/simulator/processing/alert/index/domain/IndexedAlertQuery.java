package com.silenteight.simulator.processing.alert.index.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.processing.alert.index.domain.exception.IndexedAlertEntityNotFoundException;
import com.silenteight.simulator.processing.alert.index.dto.IndexedAlertDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class IndexedAlertQuery {

  @NonNull
  private final IndexedAlertRepository repository;

  public List<IndexedAlertDto> findAllByAnalysisName(@NonNull String analysisName) {
    log.debug("Getting IndexedAlertDto by analysisName={}", analysisName);

    return repository
        .findAllByAnalysisName(analysisName)
        .stream()
        .map(IndexedAlertEntity::toDto)
        .collect(toList());
  }

  public String getAnalysisNameByRequestId(@NonNull String requestId) {
    log.debug("Getting analysisName by requestId={}", requestId);

    return repository
        .findAnalysisNameUsingRequestId(requestId)
        .orElseThrow(() -> new IndexedAlertEntityNotFoundException(requestId));
  }

  public IndexedAlertEntity getIndexedAlertEntityByRequestId(@NonNull String requestId) {
    log.debug("Getting IndexedAlertEntity by requestId={}", requestId);

    return repository
        .findByRequestId(requestId)
        .orElseThrow(() -> new IndexedAlertEntityNotFoundException(requestId));
  }

  public long count(@NonNull String analysisName, @NonNull List<State> states) {
    log.debug("Counting IndexedAlertEntity by analysisName={} and states={}",
        analysisName, states);

    return repository.countAllByAnalysisNameAndStateIn(analysisName, states);
  }

  public long sumAllAlertsCountWithAnalysisName(@NonNull String analysisName) {
    log.debug("Summing all alerts with analysisName={}", analysisName);

    return repository.sumAllAlertsCountWithAnalysisName(analysisName);
  }
}
