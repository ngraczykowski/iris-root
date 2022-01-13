package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.category.port.CreateCategoriesUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class CreateCategoriesValuesService implements CreateCategoriesUseCase {

  @Override
  public List<AgentInput> createCategoryValues(
      List<EtlHit> etlHits, RegisterAlertResponse registerAlert) {
    return List.of();
  }
}
