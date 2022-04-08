package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.common.event.AlreadySolvedAlertEvent;
import com.silenteight.payments.bridge.svb.learning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.learning.step.etl.LearningProcessedAlert.LearningProcessedAlertBuilder;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.silenteight.payments.bridge.svb.learning.domain.AlertProcessingResult.FAILED;
import static com.silenteight.payments.bridge.svb.learning.domain.AlertProcessingResult.SUCCESSFUL;

@RequiredArgsConstructor
@Slf4j
class EtlAlertProcessor implements ItemProcessor<AlertComposite, LearningProcessedAlert> {

  private final FindRegisteredAlertUseCase findRegisteredAlertUseCase;
  private final ProcessRegisteredService processRegisteredService;
  private final ProcessUnregisteredService processUnregisteredService;
  private final ApplicationEventPublisher eventPublisher;
  private final Long jobId;
  private final String fileName;
  private final AtomicLong learningProcessGuage;
  private final AtomicLong registeredAlertsProcessGuage;

  EtlAlertProcessor(
      FindRegisteredAlertUseCase findRegisteredAlertUseCase,
      ProcessRegisteredService processRegisteredService,
      ProcessUnregisteredService processUnregisteredService,
      ApplicationEventPublisher eventPublisher, Long jobId,
      final String fileName,
      MeterRegistry meterRegistry
  ) {
    this.findRegisteredAlertUseCase = findRegisteredAlertUseCase;
    this.processRegisteredService = processRegisteredService;
    this.processUnregisteredService = processUnregisteredService;
    this.eventPublisher = eventPublisher;
    this.jobId = jobId;
    this.fileName = fileName;
    this.learningProcessGuage = meterRegistry.gauge("learning.alert.hits.size", new AtomicLong(0L));
    this.registeredAlertsProcessGuage =
        meterRegistry.gauge("learning.registered.alerts.size", new AtomicLong(0L));
  }

  @Override
  public LearningProcessedAlert process(AlertComposite alertComposite) {
    this.learningProcessGuage.set(alertComposite.getHits().size());
    var solvingRegistered =
        findRegisteredAlertUseCase.find(List.of(alertComposite.toFindRegisterAlertRequest()));

    this.registeredAlertsProcessGuage.set(solvingRegistered.size());
    if (solvingRegistered.isEmpty()) {
      return processUnregistered(alertComposite);
    }

    return processRegistered(alertComposite, solvingRegistered);
  }

  LearningProcessedAlert processRegistered(
      AlertComposite alertComposite, List<RegisteredAlert> solvingRegistered) {
    var resultBuilder = createBuilder(alertComposite);
    try {
      eventPublisher.publishEvent(new AlreadySolvedAlertEvent(solvingRegistered.size()));
      processRegisteredService.process(alertComposite, solvingRegistered);
      return resultBuilder.result(SUCCESSFUL.toString()).build();
    } catch (UnsupportedMessageException e) {
      log.warn("Failed to process alert = {}", alertComposite.getSystemId(), e);
      return createFailedResponse(resultBuilder, e);
    } catch (Exception e) {
      log.error("Failed to process alert = {}", alertComposite.getSystemId(), e);
      return createFailedResponse(resultBuilder, e);
    }
  }

  LearningProcessedAlert processUnregistered(AlertComposite alertComposite) {
    var resultBuilder = createBuilder(alertComposite);

    try {
      processUnregisteredService.process(alertComposite, jobId);
      return resultBuilder.result(SUCCESSFUL.toString()).build();
    } catch (UnsupportedMessageException e) {
      log.warn("Failed to process alert = {}", alertComposite.getSystemId(), e);
      return createFailedResponse(resultBuilder, e);
    } catch (Exception e) {
      log.error("Failed to process alert = {}", alertComposite.getSystemId(), e);
      return createFailedResponse(resultBuilder, e);
    }
  }

  private LearningProcessedAlertBuilder createBuilder(AlertComposite alertComposite) {
    return LearningProcessedAlert
        .builder()
        .fileName(fileName)
        .jobId(jobId)
        .fkcoVSystemId(alertComposite.getSystemId());
  }

  private static LearningProcessedAlert createFailedResponse(
      LearningProcessedAlertBuilder resultBuilder, Exception e) {
    return resultBuilder
        .result(FAILED.toString())
        .errorMessage(e.getMessage())
        .build();
  }
}
