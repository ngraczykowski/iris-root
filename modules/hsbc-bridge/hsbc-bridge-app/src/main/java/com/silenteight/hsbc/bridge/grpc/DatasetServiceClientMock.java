package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.UUID;

@Slf4j
class DatasetServiceClientMock implements DatasetServiceClient {

  @Override
  public String createDataset(Collection<String> alerts) {
    return "datasets/" + UUID.randomUUID();
  }
}
