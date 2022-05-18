package com.silenteight.payments.bridge.firco.retention.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.payments.bridge.firco.recommendation.port.DeleteRecommendationUseCase;
import com.silenteight.payments.bridge.firco.retention.port.incoming.AlertRetentionUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
class AlertRetentionService implements AlertRetentionUseCase {

  private final AlertMessageUseCase alertMessageUseCase;
  private final DeleteRecommendationUseCase deleteRecommendationUseCase;

  @Override
  @Transactional
  public void invoke(List<UUID> alertMessageIds) {
    deleteRecommendationUseCase.delete(alertMessageIds);
    alertMessageUseCase.delete(alertMessageIds);
  }
}
