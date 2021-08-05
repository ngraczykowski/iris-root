package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsServiceClient;

public interface HistoricalFeatureClientValuesRetriever<F> extends Retriever {

  F retrieve(
      MatchData matchData, HistoricalDecisionsServiceClient historicalDecisionsServiceClient);
}
