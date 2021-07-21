package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

public interface IndividualComposite {

  @Nullable
  CustomerIndividual getCustomerIndividual();

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
        Stream.concat(getWorldCheckIndividuals().stream(), getPrivateListIndividuals().stream())
            .map(ListRecordId::getListRecordId).findFirst();

    return listRecordId.or(this::getCtrpScreeningIndividualId);
  }

  private Optional<String> getCtrpScreeningIndividualId() {
    return getCtrpScreeningIndividuals().stream()
        .map(CtrpScreening::getCountryCode)
        .findFirst();
  }
}
