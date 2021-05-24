package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

public interface FeatureClientValuesRetriever<F> extends Retriever {

  F retrieve(MatchData matchData, NameInformationServiceClient nameInformationServiceClient);
}
