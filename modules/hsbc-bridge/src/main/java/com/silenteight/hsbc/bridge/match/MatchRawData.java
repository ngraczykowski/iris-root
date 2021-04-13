package com.silenteight.hsbc.bridge.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL;
import com.silenteight.hsbc.bridge.domain.EntityComposite;
import com.silenteight.hsbc.bridge.domain.IndividualComposite;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.util.Objects.nonNull;

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
    return nonNull(individualComposite);
  }
}
