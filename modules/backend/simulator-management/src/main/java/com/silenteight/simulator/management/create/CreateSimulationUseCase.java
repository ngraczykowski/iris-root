package com.silenteight.simulator.management.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.dataset.common.DatasetResource;
import com.silenteight.simulator.dataset.domain.DatasetExternalResourceNameProvider;
import com.silenteight.simulator.dataset.domain.DatasetValidator;
import com.silenteight.simulator.management.domain.SimulationService;

import java.util.Set;

@Slf4j
@AllArgsConstructor
public class CreateSimulationUseCase {

  @NonNull
  private final ModelService modelService;

  @NonNull
  private final AnalysisService analysisService;

  @NonNull
  private final DatasetExternalResourceNameProvider externalResourceNameProvider;

  @NonNull
  private final DatasetValidator datasetValidator;

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CreateSimulationRequest request) {
    request.preAudit(auditingLogger::log);
    datasetValidator.assertAllDatasetsActive(request.getDatasets());

    SolvingModel model = modelService.getModel(request.getModel());
    Analysis analysis = runAnalysis(model);
    storeSimulation(request, analysis.getName());
    addDatasetsToAnalysis(analysis.getName(), request.getDatasets());

    request.postAudit(auditingLogger::log);
  }

  private Analysis runAnalysis(@NonNull SolvingModel model) {
    Analysis analysis = analysisService.createAnalysis(model);
    log.debug("Run analysis={}", analysis);
    return analysis;
  }

  private void storeSimulation(CreateSimulationRequest request, String analysisName) {
    simulationService.createSimulation(request, request.getDatasets(), analysisName);
  }

  private void addDatasetsToAnalysis(@NonNull String analysis, @NonNull Set<String> datasets) {
    datasets
        .stream()
        .map(DatasetResource::fromResourceName)
        .map(externalResourceNameProvider::getExternalResourceName)
        .forEach(resourceName -> analysisService.addDatasetToAnalysis(analysis, resourceName));
  }
}
