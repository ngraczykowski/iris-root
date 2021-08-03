package com.silenteight.payments.bridge.datasource.feature;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateFeatureUseCase {

  private final FeatureDataAccess featureDataAccess;

  void createFeature(Object featureObject) {
    featureDataAccess.saveFeature(featureObject);
  }
}
