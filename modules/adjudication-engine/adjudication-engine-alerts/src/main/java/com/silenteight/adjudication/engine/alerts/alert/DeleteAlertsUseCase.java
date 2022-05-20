package com.silenteight.adjudication.engine.alerts.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
class DeleteAlertsUseCase {

  @NonNull
  private final AlertRepository repository;

  int delete(List<Long> alertIds) {
    return repository.deleteAllByIdIn(alertIds);
  }
}
