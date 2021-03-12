package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.bridge.domain.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRawData {

  List<CountryCtrpScreening> countryCtrpScreeningEntities;
  List<CountryCtrpScreening> countryCtrpScreeningIndividuals;
  List<CustomerEntities> customerEntities;
  List<CustomerIndividuals> customerIndividuals;
  List<WorldCheckEntities> worldCheckEntities;
  List<WorldCheckIndividuals> worldCheckIndividuals;
}
