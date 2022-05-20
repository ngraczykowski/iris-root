package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.List;
import java.util.Map;

public interface DateFeatureValuesRetriever<F> extends Retriever {

  F retrieve(MatchData matchData, Map<String, List<String>> watchlistTypes);
}
