package com.silenteight.adjudication.api.library.v1.analysis;

public interface AnalysisServiceClient {

  AnalysisDatasetOut addDataset(AddDatasetIn request);

  CreateAnalysisOut createAnalysis(CreateAnalysisIn request);

  GetAnalysisOut getAnalysis(String analysis);

  AddAlertsToAnalysisOut addAlertsToAnalysis(AddAlertsToAnalysisIn request);
}
