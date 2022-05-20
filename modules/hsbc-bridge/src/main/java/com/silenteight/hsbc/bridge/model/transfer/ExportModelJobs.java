package com.silenteight.hsbc.bridge.model.transfer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelType;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
class ExportModelJobs {

  private final String address;
  private final ModelClient jenkinsModelClient;
  private final GetModelUseCase getModelUseCase;

  @Scheduled(cron = "${silenteight.bridge.model.name-model-export.cron}")
  void exportNameModel() {
    log.info("Running exportNameModel job");

    exportModel(ModelType.NAME_ALIASES);
  }

  @Scheduled(cron = "${silenteight.bridge.model.is-pep-procedural-model-export.cron}")
  void exportIsPepProceduralModel() {
    log.info("Running exportIsPepProceduralModel job");

    exportModel(ModelType.IS_PEP_PROCEDURAL);
  }

  @Scheduled(cron = "${silenteight.bridge.model.is-pep-historical-model-export.cron}")
  void exportIsPepHistoricalModel() {
    log.info("Running exportIsPepHistoricalModel job");

    exportModel(ModelType.IS_PEP_HISTORICAL);
  }

  private void exportModel(@NonNull ModelType modelType) {
    try {
      var modelInformation = getModelUseCase.getModel(modelType);
      var modelInfo = ModelInfoCreator.of(modelInformation).create(address);
      jenkinsModelClient.updateModel(modelInfo);
    } catch (RuntimeException e) {
      log.error("Export model failed", e);
    }
  }
}
