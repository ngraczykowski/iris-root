package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.port.CreateDatasourceInputsUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateDatasourceInputs implements CreateDatasourceInputsUseCase {

  @Override
  public void processStructured(AeAlert alert, List<HitData> hitsData) {

  }

  @Override
  public void processUnstructured(
      String alertName, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {

  }
}
