package com.silenteight.hsbc.datasource.category;

import com.silenteight.hsbc.bridge.match.MatchRawData;

import java.util.List;

interface CategoryValueRetriever {

  List<String> retrieve(MatchRawData matchRawData);
}
