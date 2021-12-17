package com.silenteight.bridge.core.registration.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService;
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository;
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class RegistrationService {

  private final DefaultModelService defaultModelService;
  private final AnalysisService analysisService;
  private final BatchRepository batchRepository;

  public BatchId register(RegisterBatchCommand registerBatchCommand) {
    return batchRepository.findById(registerBatchCommand.id())
        .map(this::logIfAlreadyExists)
        .or(() -> this.registerNew(registerBatchCommand))
        .map(BatchId::from)
        .orElseThrow();
  }

  private Batch logIfAlreadyExists(Batch batch) {
    log.info("Batch for the given id: {} already exists.", batch.id());
    return batch;
  }

  private Optional<Batch> registerNew(RegisterBatchCommand registerBatchCommand) {
    log.info("Registering new batch with id: {}", registerBatchCommand.id());
    var defaultModel = defaultModelService.getForSolving();
    var analysis = analysisService.create(defaultModel);
    var batch =
        Batch.newOne(registerBatchCommand.id(), analysis.name(), registerBatchCommand.alertCount());
    return Optional.of(batchRepository.create(batch));
  }

}
