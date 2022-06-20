/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.support;

import java.util.ArrayList;
import java.util.List;

public class StringsList extends ArrayList<String> {

  private static final long serialVersionUID = 5093206154129220048L;

  private static final String DELIMITER = ",";

  public List<String> asList() {
    return new ArrayList<>(this);
  }

  @Override
  public String toString() {
    return String.join(DELIMITER, this);
  }
}
