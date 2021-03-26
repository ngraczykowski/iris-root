package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.model.api.v1.SolvingModel;

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

    SolvingModel model = modelService.getModel(request.getModelName());
    Analysis analysis = analysisService.createAnalysis(model);
    analysisService.addDatasetToAnalysis(analysis.getName(), request.getDatasetName());
    simulationService.createSimulation(request, analysis.getName());

    request.postAudit(auditingLogger::log);
  }
}
