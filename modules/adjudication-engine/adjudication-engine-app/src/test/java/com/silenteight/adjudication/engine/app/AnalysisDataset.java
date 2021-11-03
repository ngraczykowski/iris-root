package com.silenteight.adjudication.engine.app;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Dataset;

@Value
@Builder
public class AnalysisDataset {

  Analysis analysis;

  Dataset dataset;
}
