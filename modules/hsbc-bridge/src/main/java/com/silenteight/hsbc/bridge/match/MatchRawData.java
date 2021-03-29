package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.bridge.domain.*;

import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRawData {

  CasesWithAlertURL caseWithAlertURL;
  List<CountryCtrpScreening> countryCtrpScreeningEntities;
  List<CountryCtrpScreening> countryCtrpScreeningIndividuals;
  List<CustomerEntities> customerEntities;
  List<CustomerIndividuals> customerIndividuals;
  List<WorldCheckEntities> worldCheckEntities;
  List<WorldCheckIndividuals> worldCheckIndividuals;

  public boolean isIndividual() {
    return CollectionUtils.isEmpty(customerEntities);
  }
}
