package com.silenteight.customerbridge.common.gender;

import java.util.List;

import static java.util.Arrays.asList;

class NameGenderDataStub implements NameGenderData {

  @Override
  public List<String> getMaleNames() {
    return asList("Kamil", "Piotr", "Łukasz");
  }

  @Override
  public List<String> getFemaleNames() {
    return asList("Aleksandra", "Joanna", "Emilia");
  }
}
