package com.silenteight.adjudication.engine.analysis.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.*;
import com.silenteight.adjudication.engine.analysis.analysis.AnalysisFacade;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.singletonList;

@RequiredArgsConstructor
@Service
class AnalysisService {

  @NonNull
  private final AnalysisFacade analysisFacade;

  Analysis createAnalysis(CreateAnalysisRequest request) {
    return analysisFacade.createAndGetAnalysis(request.getAnalysis());
  }

  AnalysisDataset addDataset(AddDatasetRequest request) {
    return analysisFacade.addDatasets(
        request.getAnalysis(), singletonList(request.getDataset())).get(0);
  }

  BatchAddDatasetsResponse batchAddDatasets(BatchAddDatasetsRequest request) {
    List<AnalysisDataset> datasets = analysisFacade.addDatasets(
        request.getAnalysis(), request.getDatasetsList());
    return BatchAddDatasetsResponse.newBuilder().addAllAnalysisDatasets(datasets).build();
  }

  Analysis getAnalysis(GetAnalysisRequest request) {
    return analysisFacade.getAnalysis(request.getAnalysis());
  }
}
