package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

public interface IsPepFeatureValuesRetriever<F> extends Retriever {

  F retrieve(MatchData matchData, String matchName);
}
