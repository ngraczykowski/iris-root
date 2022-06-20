/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gender;

import java.util.Collections;
import java.util.List;

class EmptyNameGenderData implements NameGenderData {

  @Override
  public List<String> getMaleNames() {
    return Collections.emptyList();
  }

  @Override
  public List<String> getFemaleNames() {
    return Collections.emptyList();
  }
}
