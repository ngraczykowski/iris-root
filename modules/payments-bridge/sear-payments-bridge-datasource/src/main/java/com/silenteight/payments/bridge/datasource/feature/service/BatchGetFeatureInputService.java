package com.silenteight.payments.bridge.datasource.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchFeatureInputResponse;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchGetFeatureInputUseCase;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
class BatchGetFeatureInputService implements BatchGetFeatureInputUseCase {

  @Override
  public void batchGetFeatureInput(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<BatchFeatureInputResponse> consumer) {

    // TODO(ahaczewski): Implement reading from the database table pb_match_feature_input
    //  and converting to appropriate batch response types from `payload` column, based on
    //  `payload_type` name.
    throw new UnsupportedOperationException("TODO: Please implement batchGetFeatureInput");
  }
}
