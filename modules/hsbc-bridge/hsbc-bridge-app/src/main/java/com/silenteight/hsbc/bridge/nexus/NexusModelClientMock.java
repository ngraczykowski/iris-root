package com.silenteight.hsbc.bridge.nexus;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.model.transfer.RepositoryClient;

import java.io.IOException;

@Slf4j
class NexusModelClientMock implements RepositoryClient {

  @Override
  public byte[] updateModel(String uri) throws IOException {
    log.info("Request for updating model sent. URI: {}", uri);
    return new byte[0];
  }
}
