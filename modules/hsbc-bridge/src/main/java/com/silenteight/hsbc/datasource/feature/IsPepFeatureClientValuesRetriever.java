package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.ispep.IsPepServiceClient;

public interface IsPepFeatureClientValuesRetriever<F> extends Retriever {

  F retrieve(MatchData matchData, IsPepServiceClient isPepServiceClient);
}
