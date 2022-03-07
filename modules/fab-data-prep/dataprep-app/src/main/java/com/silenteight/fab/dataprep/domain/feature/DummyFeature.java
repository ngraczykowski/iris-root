package com.silenteight.fab.dataprep.domain.feature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyFeature implements Feature {

  @Override
  public void createFeatureInput(FeatureInputsCommand featureInputsCommand) {
    log.debug(
        "Creating feature input for alert id {} and batch id {}",
        featureInputsCommand.getExtractedAlert().getAlertId(),
        featureInputsCommand.getBatchId());
  }
}
