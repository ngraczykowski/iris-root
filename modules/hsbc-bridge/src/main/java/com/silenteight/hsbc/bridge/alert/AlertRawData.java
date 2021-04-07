package com.silenteight.hsbc.bridge.alert;

import lombok.*;

import com.silenteight.hsbc.bridge.domain.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertRawData {

  private List<CountryCtrpScreening> countryCtrpScreeningIndividuals;
  private List<CustomerEntities> customerEntities;
  private List<WorldCheckEntities> worldCheckEntities;
  private List<CustomerIndividuals> customerIndividuals;
  private List<WorldCheckIndividuals> worldCheckIndividuals;
  private List<CaseComments> caseComments;
  private List<Relationships> relationships;
  private List<PrivateListIndividuals> privateListIndividuals;
  private List<PrivateListEntities> privateListEntities;
  private List<CaseHistory> caseHistory;
  private List<CountryCtrpScreening> countryCtrpScreeningEntities;
  private List<CasesWithAlertURL> casesWithAlertURL;

  @JsonIgnore
  public CasesWithAlertURL getFirstCaseWithAlertURL() {
    return getCasesWithAlertURL().get(0);
  }
}
