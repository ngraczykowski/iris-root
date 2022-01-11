package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningRegisteredAlert;

import org.springframework.batch.item.ItemProcessor;

import java.util.List;

@RequiredArgsConstructor
class EtlAlertProcessor implements ItemProcessor<AlertComposite, LearningRegisteredAlert> {

  private final FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  private final ProcessRegisteredService processRegisteredService;
  private final ProcessUnregisteredService processUnregisteredService;
  private final Long jobId;

  @Override
  public LearningRegisteredAlert process(AlertComposite alertComposite) {
    var solvingRegistered =
        findRegisteredAlertUseCase.find(List.of(alertComposite.toFindRegisterAlertRequest()));

    if (!solvingRegistered.isEmpty()) {
      return processRegisteredService.process(alertComposite, solvingRegistered.get(0));
    }

    return processUnregisteredService.process(alertComposite, jobId);
  }
}
