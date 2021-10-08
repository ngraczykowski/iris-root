package com.silenteight.hsbc.datasource.datamodel;

import java.util.Optional;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public interface MatchData extends EntityComposite, IndividualComposite {

  CaseInformation getCaseInformation();

  default boolean isIndividual() {
    return isNotEmpty(getCustomerIndividuals());
  }

  default boolean isEntity() {
    return isNotEmpty(getCustomerEntities());
  }

  default Optional<String> getWatchlistId() {
    return isIndividual() ? getIndividualWatchlistId() : getEntityWatchlistId();
  }

  default Optional<WatchlistType> getWatchlistType() {
    return isIndividual() ? getIndividualWatchlistType() : getEntityWatchlistType();
  }
}
