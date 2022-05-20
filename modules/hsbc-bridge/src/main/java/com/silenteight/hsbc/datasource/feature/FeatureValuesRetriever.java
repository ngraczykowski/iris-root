package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

public interface FeatureValuesRetriever<F> extends Retriever {

  F retrieve(MatchData matchData);
}
