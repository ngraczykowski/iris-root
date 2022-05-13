package com.silenteight.universaldatasource.app.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateAgentInputTypeUseCase implements CommandLineRunner {

  private final FeatureDataAccess featureDataAccess;

  @Override
  public void run(String... args) {

    log.debug("Checking, if agent input type data needs to be updated");

    if (featureDataAccess.isAgentInputTypeUpdated() > 0) {
      log.debug("Updating agent input types");

      int updatedAgentInputTypeCount = featureDataAccess.updateAgentInputType();

      log.debug("Finished updating agent input types, count={}", updatedAgentInputTypeCount);
    } else {
      log.debug("Agent input types are up to date");
    }
  }
}
