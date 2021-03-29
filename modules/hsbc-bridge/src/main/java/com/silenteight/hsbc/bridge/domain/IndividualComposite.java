package com.silenteight.hsbc.bridge.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class IndividualComposite {

  CustomerIndividuals customerIndividuals;
  List<WorldCheckIndividuals> worldCheckIndividuals;
  List<PrivateListIndividuals> privateListIndividuals;
  List<CountryCtrpScreening> countryCtrpScreeningIndividuals;
}
