package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public interface IndividualComposite {

  List<CustomerIndividual> getCustomerIndividuals();

  List<WorldCheckIndividual> getWorldCheckIndividuals();

  List<PrivateListIndividual> getPrivateListIndividuals();

  List<CtrpScreening> getCtrpScreeningIndividuals();

  default boolean hasWorldCheckIndividuals() {
    return Objects.nonNull(getWorldCheckIndividuals()) && !getWorldCheckIndividuals().isEmpty();
  }

  default boolean hasPrivateListIndividuals() {
    return Objects.nonNull(getPrivateListIndividuals()) && !getPrivateListIndividuals().isEmpty();
  }

  default boolean hasCtrpScreeningIndividuals() {
    return Objects.nonNull(getCtrpScreeningIndividuals()) && !getCtrpScreeningIndividuals().isEmpty();
  }

  default Optional<String> getIndividualWatchlistId() {
    var listRecordId =
        Stream.concat(getWorldCheckIndividuals().stream(), getPrivateListIndividuals().stream())
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
      return Optional.of(WatchlistType.WORLDCHECK_INDIVIDUALS);
    } else if (!getPrivateListIndividuals().isEmpty()) {
      return Optional.of(WatchlistType.PRIVATE_LIST_INDIVIDUALS);
    } else if (!getCtrpScreeningIndividuals().isEmpty()) {
      return Optional.of(WatchlistType.CTRPPRHB_LIST_INDIVIDUALS);
    } else
      return Optional.empty();
  }
}
