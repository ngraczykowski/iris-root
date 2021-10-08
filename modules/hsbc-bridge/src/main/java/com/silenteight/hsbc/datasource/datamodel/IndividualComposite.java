package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import java.util.Optional;

import static com.silenteight.hsbc.datasource.datamodel.WatchlistType.CTRPPRHB_LIST_INDIVIDUALS;
import static com.silenteight.hsbc.datasource.datamodel.WatchlistType.PRIVATE_LIST_INDIVIDUALS;
import static com.silenteight.hsbc.datasource.datamodel.WatchlistType.WORLDCHECK_INDIVIDUALS;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Stream.concat;

public interface IndividualComposite {

  List<CustomerIndividual> getCustomerIndividuals();

  List<WorldCheckIndividual> getWorldCheckIndividuals();

  List<PrivateListIndividual> getPrivateListIndividuals();

  List<CtrpScreening> getCtrpScreeningIndividuals();

  default boolean hasWorldCheckIndividuals() {
    return nonNull(getWorldCheckIndividuals()) && !getWorldCheckIndividuals().isEmpty();
  }

  default boolean hasPrivateListIndividuals() {
    return nonNull(getPrivateListIndividuals()) && !getPrivateListIndividuals().isEmpty();
  }

  default boolean hasCtrpScreeningIndividuals() {
    return nonNull(getCtrpScreeningIndividuals()) && !getCtrpScreeningIndividuals().isEmpty();
  }

  default Optional<String> getIndividualWatchlistId() {
    var listRecordId =
        concat(getWorldCheckIndividuals().stream(), getPrivateListIndividuals().stream())
            .map(ListRecordId::getListRecordId).findFirst();

    return listRecordId.or(this::getCtrpScreeningIndividualId);
  }

  private Optional<String> getCtrpScreeningIndividualId() {
    return getCtrpScreeningIndividuals().stream()
        .map(CtrpScreening::getCountryCode)
        .findFirst();
  }

  default Optional<WatchlistType> getIndividualWatchlistType() {
    if (!getWorldCheckIndividuals().isEmpty()) {
      return of(WORLDCHECK_INDIVIDUALS);
    } else if (!getPrivateListIndividuals().isEmpty()) {
      return of(PRIVATE_LIST_INDIVIDUALS);
    } else if (!getCtrpScreeningIndividuals().isEmpty()) {
      return of(CTRPPRHB_LIST_INDIVIDUALS);
    } else
      return empty();
  }
}
