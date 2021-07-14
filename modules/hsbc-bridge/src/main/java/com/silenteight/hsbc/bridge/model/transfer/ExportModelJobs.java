package com.silenteight.hsbc.bridge.model.transfer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.dto.ModelType;

import org.springframework.scheduling.annotation.Scheduled;

import static com.silenteight.hsbc.bridge.model.dto.ModelType.IS_PEP_HISTORICAL;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.IS_PEP_PROCEDURAL;
import static com.silenteight.hsbc.bridge.model.dto.ModelType.NAME_ALIASES;

@RequiredArgsConstructor
@Slf4j
class ExportModelJobs {

  private final String address;
  private final ModelClient jenkinsModelClient;
  private final GetModelUseCase getModelUseCase;

  @Scheduled(cron = "${silenteight.bridge.model.is-pep-historical-model-export.cron}")
  void exportIsPepHistoricalModel() {
    log.info("Running exportIsPepHistoricalModel job");

    exportModel(IS_PEP_HISTORICAL);
  }

  @Scheduled(cron = "${silenteight.bridge.model.is-pep-procedural-model-export.cron}")
  void exportIsPepProceduralModel() {
    log.info("Running exportIsPepProceduralModel job");

    exportModel(IS_PEP_PROCEDURAL);
  }

  @Scheduled(cron = "${silenteight.bridge.model.name-model-export.cron}")
  void exportNameModel() {
    log.info("Running exportNameModel job");

    exportModel(NAME_ALIASES);
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
