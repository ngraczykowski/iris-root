package com.silenteight.hsbc.datasource.feature.country;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

import java.util.stream.Stream;

public interface OtherCountryQuery {

  Stream<String> apCustomerIndividualOtherCountries();

  Stream<String> apCustomerEntityOtherCountries();

  Stream<String> mpWorldCheckIndividualsOtherCountries();

  Stream<String> mpWorldCheckEntitiesOtherCountries();

  Stream<String> mpPrivateListIndividualsOtherCountries();

  Stream<String> mpPrivateListEntitiesOtherCountries();

  Stream<String> mpCtrpScreeningIndividualsOtherCountries();

  Stream<String> mpCtrpScreeningEntitiesOtherCountries();

  interface Factory {

    OtherCountryQuery create(MatchData matchData);
  }
}
