package com.silenteight.hsbc.datasource.feature.ispep;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface IsPepQuery {

  String apWorldCheckIndividualsExtractEdqLobCountryCode();

  String apWorldCheckEntitiesExtractEdqLobCountryCode();

  String mpWorldCheckIndividualsFurtherInformation();

  Stream<String> mpWorldCheckIndividualsLinkedTo();

  Stream<String> mpWorldCheckIndividualsCountryCodes();

  String mpWorldCheckEntitiesFurtherInformation();

  Stream<String> mpWorldCheckEntitiesLinkedTo();

  Stream<String> mpWorldCheckEntitiesCountryCodes();

  interface Factory {

    IsPepQuery create(MatchData matchData);
  }
}
