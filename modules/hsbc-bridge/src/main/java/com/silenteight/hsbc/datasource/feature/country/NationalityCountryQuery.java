package com.silenteight.hsbc.datasource.feature.country;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;

import java.util.stream.Stream;

public interface NationalityCountryQuery {

  Stream<String> apLine4DocumentCountry();

  Stream<String> apFieldsIndividualCountries();

  Stream<String> mpDocumentCountries();

  Stream<String> mpWorldCheckCountries();

  interface Factory {

    NationalityCountryQuery create(IndividualComposite individualComposite);
  }
}
