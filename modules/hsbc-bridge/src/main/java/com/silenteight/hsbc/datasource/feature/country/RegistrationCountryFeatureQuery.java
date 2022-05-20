package com.silenteight.hsbc.datasource.feature.country;

import com.silenteight.hsbc.datasource.datamodel.EntityComposite;

import java.util.stream.Stream;

public interface RegistrationCountryFeatureQuery {

  Stream<String> worldCheckEntitiesRegistrationCountries();

  Stream<String> privateListEntitiesRegistrationCountries();

  Stream<String> ctrpScreeningEntitiesRegistrationCountries();

  Stream<String> getCustomerEntityRegistrationCountries();

  Stream<String> getWatchlistEntityRegistrationCountries();

  interface Factory {

    RegistrationCountryFeatureQuery create(EntityComposite entityComposite);
  }
}
