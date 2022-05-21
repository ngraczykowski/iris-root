package com.silenteight.hsbc.datasource.category;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.List;

interface CategoryValueRetriever {

  List<String> retrieve(MatchData matchData);
}
