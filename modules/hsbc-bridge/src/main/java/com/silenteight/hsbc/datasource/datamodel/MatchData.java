package com.silenteight.hsbc.datasource.datamodel;

import static java.util.Objects.nonNull;

public interface MatchData extends EntityComposite, IndividualComposite {

  CaseInformation getCaseInformation();

  default boolean isIndividual() {
    return nonNull(getCustomerIndividual());
  }

  default boolean isEntity() {
    return !isIndividual();
  }
}
