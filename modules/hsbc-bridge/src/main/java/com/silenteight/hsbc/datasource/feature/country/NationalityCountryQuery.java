package com.silenteight.hsbc.datasource.feature.country;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;

import java.util.stream.Stream;

public interface NationalityCountryQuery {

  Stream<String> apDocumentCountries();

  Stream<String> apFieldsIndividualCountries();

  Stream<String> mpDocumentCountries();

  Stream<String> mpWorldCheckIndividualCountries();

  Stream<String> mpPrivateListIndividualsCountries();

  Stream<String> mpCtrpScreeningIndividualsCountries();

  Stream<String> getWatchlistIndividualsNationalityCountry();

  interface Factory {

    NationalityCountryQuery create(IndividualComposite individualComposite);
  }
}
