package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;

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
}
