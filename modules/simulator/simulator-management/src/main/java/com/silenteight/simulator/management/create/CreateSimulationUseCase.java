package com.silenteight.simulator.management.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.dataset.common.DatasetResource;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
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
  private final DatasetQuery datasetQuery;

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CreateSimulationRequest request) {
    request.preAudit(auditingLogger::log);

    SolvingModel model = modelService.getModel(request.getModel());
    Analysis analysis = runAnalysis(model);
    storeSimulation(request, request.getDatasets(), analysis.getName());
    addDatasetsToAnalysis(analysis.getName(), request.getDatasets());

    request.postAudit(auditingLogger::log);
  }

  private Analysis runAnalysis(@NonNull SolvingModel model) {
    Analysis analysis = analysisService.createAnalysis(model);
    log.debug("Run analysis={}", analysis);
    return analysis;
  }

  private void storeSimulation(
      CreateSimulationRequest request, Set<String> datasets, String analysis) {

    simulationService.createSimulation(request, datasets, analysis);
  }

  private void addDatasetsToAnalysis(@NonNull String analysis, @NonNull Set<String> datasets) {
    datasets
        .stream()
        .map(DatasetResource::fromResourceName)
        .map(datasetQuery::getExternalResourceName)
        .forEach(resourceName -> analysisService.addDatasetToAnalysis(analysis, resourceName));
  }
}
