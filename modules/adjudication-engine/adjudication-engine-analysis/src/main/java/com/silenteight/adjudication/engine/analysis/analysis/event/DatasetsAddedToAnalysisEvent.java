package com.silenteight.adjudication.engine.analysis.analysis.event;

import lombok.Value;

import java.io.Serializable;

@Value
public class DatasetsAddedToAnalysisEvent implements Serializable {

  private static final long serialVersionUID = 7784370879323221192L;

  long analysisId;

  long[] datasetIds;
}
