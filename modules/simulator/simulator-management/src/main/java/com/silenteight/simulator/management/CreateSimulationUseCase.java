package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.model.api.v1.SolvingModel;

import java.util.Set;

@AllArgsConstructor
public class CreateSimulationUseCase {

  @NonNull
  private final ModelService modelService;

  @NonNull
  private final AnalysisService analysisService;

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CreateSimulationRequest request) {
    request.preAudit(auditingLogger::log);

    SolvingModel model = modelService.getModel(request.getModel());
    Analysis analysis = runAnalysis(model, request.getDatasets());
    storeSimulation(request, request.getDatasets(), analysis.getName());

    request.postAudit(auditingLogger::log);
  }

  private Analysis runAnalysis(@NonNull SolvingModel model, @NonNull Set<String> datasets) {
    Analysis analysis = analysisService.createAnalysis(model);
    datasets.forEach(dataset -> analysisService.addDatasetToAnalysis(analysis.getName(), dataset));
    return analysis;
  }

  private void storeSimulation(
      CreateSimulationRequest request, Set<String> datasets, String analysis) {

    simulationService.createSimulation(request, datasets, analysis);
  }
}
