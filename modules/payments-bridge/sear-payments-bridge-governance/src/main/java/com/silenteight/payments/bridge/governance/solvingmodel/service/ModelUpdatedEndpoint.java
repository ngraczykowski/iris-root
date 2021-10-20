package com.silenteight.payments.bridge.governance.solvingmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.common.integration.CommonChannels;
import com.silenteight.payments.bridge.event.ModelUpdatedEvent;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import static com.silenteight.payments.bridge.governance.solvingmodel.service.AnalysisModelMapper.fromSolvingModel;

@MessageEndpoint
@RequiredArgsConstructor
@Slf4j
class ModelUpdatedEndpoint {

  @ServiceActivator(inputChannel = CommonChannels.SOLVING_MODEL_UPDATED,
      outputChannel = CommonChannels.NEW_MODEL_RECEIVED)
  ModelUpdatedEvent accept(ModelUpdatedEvent event) {
    var model = event.getData(SolvingModel.class);

    log.info("Received updated production solving model: policy={}, categories={}, features={}",
        model.getPolicyName(), model.getCategoriesList(),
        model.getFeaturesList().stream().map(Feature::getName));

    event.registerCollector(AnalysisModel.class, () -> fromSolvingModel(model));

    return event;
  }
}
