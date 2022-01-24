package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.step.etl.LearningProcessedAlert.LearningProcessedAlertBuilder;

import org.springframework.batch.item.ItemProcessor;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertProcessingResult.FAILED;
import static com.silenteight.payments.bridge.svb.newlearning.domain.AlertProcessingResult.SUCCESSFUL;

@RequiredArgsConstructor
class EtlAlertProcessor implements ItemProcessor<AlertComposite, LearningProcessedAlert> {

  private final FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  private final ProcessRegisteredService processRegisteredService;
  private final ProcessUnregisteredService processUnregisteredService;
  private final Long jobId;
  private final String fileName;

  @Override
  public LearningProcessedAlert process(AlertComposite alertComposite) {
    var solvingRegistered =
        findRegisteredAlertUseCase.find(List.of(alertComposite.toFindRegisterAlertRequest()));

    if (solvingRegistered.isEmpty()) {
      return processUnregistered(alertComposite);
    }

    return processRegistered(alertComposite, solvingRegistered.get(0));
  }

  LearningProcessedAlert processRegistered(
      AlertComposite alertComposite, RegisteredAlert solvingRegistered) {
    var resultBuilder = createBuilder(alertComposite);

    try {
      processRegisteredService.process(alertComposite, solvingRegistered);
      return resultBuilder.result(SUCCESSFUL.toString()).build();
    } catch (Exception e) {
      return resultBuilder
          .result(FAILED.toString())
          .errorMessage(e.getMessage())
          .build();
    }
  }

  LearningProcessedAlert processUnregistered(AlertComposite alertComposite) {
    var resultBuilder = createBuilder(alertComposite);

    try {
      processUnregisteredService.process(alertComposite, jobId);
      return resultBuilder.result(SUCCESSFUL.toString()).build();
    } catch (Exception e) {
      return resultBuilder
          .result(FAILED.toString())
          .errorMessage(e.getMessage())
          .build();
    }
  }

  private LearningProcessedAlertBuilder createBuilder(AlertComposite alertComposite) {
    return LearningProcessedAlert
        .builder()
        .fileName(fileName)
        .jobId(jobId)
        .fkcoVSystemId(alertComposite.getSystemId());
  }
}
