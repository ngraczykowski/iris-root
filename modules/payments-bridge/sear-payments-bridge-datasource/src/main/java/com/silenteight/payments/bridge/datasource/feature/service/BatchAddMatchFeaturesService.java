package com.silenteight.payments.bridge.datasource.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.port.incoming.AddMatchFeaturesRequest;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchAddMatchFeaturesUseCase;
import com.silenteight.sep.base.common.protocol.MessageRegistry;

import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
class BatchAddMatchFeaturesService implements BatchAddMatchFeaturesUseCase {

  private final MessageRegistry messageRegistry;

  @Override
  public void batchAddMatchFeatures(Collection<AddMatchFeaturesRequest> requests) {
    // TODO(ahaczewski): Implement with simple INSERTs to the database table pb_match_feature_input.
    throw new UnsupportedOperationException("TODO: Please implement batchAddMatchFeatures");
  }
}
