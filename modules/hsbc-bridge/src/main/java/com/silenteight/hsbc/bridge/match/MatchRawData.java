package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.domain.*;

import java.util.List;

@Value
@Builder
public class MatchRawData {

  List<CountryCtrpScreeningEntities> countryCtrpScreeningEntities;
  List<CountryCtrpScreeningIndividuals> countryCtrpScreeningIndividuals;
  List<CustomerEntities> customerEntities;
  List<CustomerIndividuals> customerIndividuals;
  List<WorldCheckEntities> worldCheckEntities;
  List<WorldCheckIndividuals> worldCheckIndividuals;
}
