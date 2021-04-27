package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.simulator.dataset.common.DatasetResource;
import com.silenteight.simulator.dataset.domain.DatasetQuery;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

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

    SolvingModel model = modelService.getModel(request.getModelName());

    Analysis analysis = runAnalysis(
        model, toExternalDatasetResourceNames(request.getDatasetNames()));
    storeSimulation(request, request.getDatasetNames(), analysis.getName());

    request.postAudit(auditingLogger::log);
  }

  @NotNull
  private Set<String> toExternalDatasetResourceNames(Set<String> datasetNames) {
    return datasetNames
        .stream()
        .map(DatasetResource::fromResourceName)
        .map(datasetQuery::getExternalResourceName)
        .collect(toSet());
  }

  private Analysis runAnalysis(SolvingModel model, @NonNull Set<String> exteranalDatasetNames) {
    Analysis analysis = analysisService.createAnalysis(model);
    exteranalDatasetNames.forEach(datasetName -> analysisService.addDatasetToAnalysis(
            analysis.getName(), datasetName));
    return analysis;
  }

  private void storeSimulation(
      CreateSimulationRequest request, Set<String> datasetNames, String analysisName) {

    simulationService.createSimulation(request, datasetNames, analysisName);
  }
}
