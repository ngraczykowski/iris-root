package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;

public interface IndividualComposite {

  CustomerIndividual getCustomerIndividual();
  List<WorldCheckIndividual> getWorldCheckIndividuals();
  List<PrivateListIndividual> getPrivateListIndividuals();
  List<CtrpScreening> getCtrpScreeningIndividuals();
}
