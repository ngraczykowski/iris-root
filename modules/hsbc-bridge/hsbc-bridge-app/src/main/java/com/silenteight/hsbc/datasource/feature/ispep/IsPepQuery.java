package com.silenteight.hsbc.datasource.feature.ispep;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface IsPepQuery {

  String apWorldCheckIndividualsExtractEdqLobCountryCode();

  String apWorldCheckEntitiesExtractEdqLobCountryCode();

  public String individualsFurtherInformation();

  public Stream<String> individualsLinkedTo();

  Stream<String> individualsCountryCodes();

  String entitiesFurtherInformation();

  Stream<String> entitiesLinkedTo();

  Stream<String> entitiesCountryCodes();

  interface Factory {

    IsPepQuery create(MatchData matchData);
  }
}
