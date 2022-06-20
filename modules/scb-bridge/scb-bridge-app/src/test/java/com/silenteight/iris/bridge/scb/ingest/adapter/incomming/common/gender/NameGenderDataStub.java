/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender;

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
