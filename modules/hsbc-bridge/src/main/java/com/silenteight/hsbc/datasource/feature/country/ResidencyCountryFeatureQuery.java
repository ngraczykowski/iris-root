package com.silenteight.hsbc.datasource.feature.country;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;

import java.util.stream.Stream;

public interface ResidencyCountryFeatureQuery {

  Stream<String> worldCheckIndividualsResidencies();

  Stream<String> privateListIndividualsResidencies();

  Stream<String> ctrpScreeningResidencies();

  Stream<String> customerIndividualResidencies();

  interface Factory {

    ResidencyCountryFeatureQuery create(IndividualComposite individualComposite);
  }
}
