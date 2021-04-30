package com.silenteight.adjudication.engine.analysis.analysis.event;

import lombok.Value;

import java.io.Serializable;

@Value
public class DatasetsAddedToAnalysisEvent implements Serializable {

  long analysisId;

  long[] datasetId;
}
