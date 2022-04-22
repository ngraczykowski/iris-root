package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.model.AlertState;
import com.silenteight.fab.dataprep.domain.model.SolvingRequest;

import org.springframework.stereotype.Service;

@Slf4j
@Service
class SolvingUseCase extends BaseUseCase {

  private final FeedingFacade feedingFacade;

  public SolvingUseCase(
      AlertService alertService,
      RegistrationService registrationService,
      FeedingFacade feedingFacade) {
    super(alertService, registrationService);
    this.feedingFacade = feedingFacade;
  }

  void handle(SolvingRequest solvingRequest) {
    registerNewAlerts(solvingRequest.getExtractedAlerts())
        .forEach(registeredAlert -> {
          feedingFacade.etlAndFeedUds(registeredAlert);
          var discriminator = registeredAlert.getDiscriminator();
          alertService.setAlertState(discriminator, AlertState.IN_UDS);
        });
  }
}
