package com.silenteight.hsbc.datasource.feature.location;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;

import java.util.stream.Stream;

public interface LocationFeatureQuery {

  Stream<String> worldCheckIndividualsResidencies();

  Stream<String> customerIndividualResidencies();

  interface Factory {

    LocationFeatureQuery create(IndividualComposite individualComposite);
  }
}
