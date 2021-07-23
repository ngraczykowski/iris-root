package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.FeatureFacade;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class LocationInputService {

  private final FeatureFacade facade;
}
