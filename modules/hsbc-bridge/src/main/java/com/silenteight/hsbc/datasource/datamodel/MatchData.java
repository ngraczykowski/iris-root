package com.silenteight.hsbc.datasource.datamodel;

import org.apache.commons.collections.CollectionUtils;

import java.util.Optional;

public interface MatchData extends EntityComposite, IndividualComposite {

  CaseInformation getCaseInformation();

  default boolean isIndividual() {
    return CollectionUtils.isNotEmpty(getCustomerIndividuals());
  }

  default boolean isEntity() {
    return CollectionUtils.isNotEmpty(getCustomerEntities());
  }

  default Optional<String> getWatchlistId() {
    return isIndividual() ? getIndividualWatchlistId() : getEntityWatchlistId();
  }

  default Optional<WatchlistType> getWatchlistType() {
    return isIndividual() ? getIndividualWatchlistType() : getEntityWatchlistType();
  }
}
