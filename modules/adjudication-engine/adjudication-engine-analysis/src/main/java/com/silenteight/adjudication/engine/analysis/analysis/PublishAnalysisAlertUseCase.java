package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.internal.v1.AddedAnalysisAlerts;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class PublishAnalysisAlertUseCase {

  @NonNull
  private final ApplicationEventPublisher applicationEventPublisher;

  void publish(AddedAnalysisAlerts addedAnalysisAlerts) {
    applicationEventPublisher.publishEvent(addedAnalysisAlerts);
  }
}
