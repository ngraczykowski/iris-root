package com.silenteight.hsbc.bridge.domain;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualComposite {

  CustomerIndividuals customerIndividuals;
  List<WorldCheckIndividuals> worldCheckIndividuals;
  List<PrivateListIndividuals> privateListIndividuals;
  List<CountryCtrpScreening> countryCtrpScreeningIndividuals;
}
