package com.silenteight.hsbc.datasource.feature.newsage;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

public interface NewsAgeQuery {

  String id();

  String type();

  String nnsIndividualsFurtherInformation();

  String nnsEntitiesFurtherInformation();


  interface Factory {
    NewsAgeQuery create(MatchData matchData);
  }
}
