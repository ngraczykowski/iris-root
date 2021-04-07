package com.silenteight.hsbc.bridge.match;

import lombok.*;

import com.silenteight.hsbc.bridge.domain.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.util.Objects.isNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MatchRawData {

  private int caseId;
  private CasesWithAlertURL caseWithAlertURL;
  private EntityComposite entityComposite;
  private IndividualComposite individualComposite;

  MatchRawData(int caseId, CasesWithAlertURL caseWithAlertURL, EntityComposite composite) {
    this.caseId = caseId;
    this.caseWithAlertURL = caseWithAlertURL;
    this.entityComposite = composite;
  }

  MatchRawData(int caseId, CasesWithAlertURL caseWithAlertURL, IndividualComposite composite) {
    this.caseId = caseId;
    this.caseWithAlertURL = caseWithAlertURL;
    this.individualComposite = composite;
  }

  @JsonIgnore
  public boolean isIndividual() {
    return isNull(entityComposite);
  }
}
