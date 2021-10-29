package com.silenteight.hsbc.datasource.feature.ispep;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface IsPepQueryV2 {

  String apWorldCheckIndividualsExtractEdqLobCountryCode();

  String apWorldCheckEntitiesExtractEdqLobCountryCode();

  String mpWorldCheckIndividualsFurtherInformation();

  Stream<String> mpWorldCheckIndividualsLinkedTo();

  String mpWorldCheckEntitiesFurtherInformation();

  Stream<String> mpWorldCheckEntitiesLinkedTo();

  interface Factory {

    IsPepQueryV2 create(MatchData matchData);
  }
}
