package com.silenteight.hsbc.datasource.datamodel;

import java.util.Optional;

import static java.util.Objects.nonNull;

public interface MatchData extends EntityComposite, IndividualComposite {

  CaseInformation getCaseInformation();

  default boolean isIndividual() {
    return nonNull(getCustomerIndividual());
  }

  default boolean isEntity() {
    return nonNull(getCustomerEntity());
  }

  default Optional<String> getWatchlistId() {
    return isIndividual() ? getIndividualWatchlistId() : getEntityWatchlistId();
  }

  default Optional<WatchlistType> getWatchlistType() {
    return isIndividual() ? getIndividualWatchlistType() : getEntityWatchlistType();
  }
}
