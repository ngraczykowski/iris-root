package com.silenteight.hsbc.bridge.adjudication;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.UUID;

@Slf4j
class DatasetServiceApiMock implements DatasetServiceApi {

  @Override
  public String createDataset(Collection<String> alerts) {
    return "datasets/" + UUID.randomUUID();
  }
}
