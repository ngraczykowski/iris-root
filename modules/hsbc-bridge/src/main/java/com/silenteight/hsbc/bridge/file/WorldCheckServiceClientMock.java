package com.silenteight.hsbc.bridge.file;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class WorldCheckServiceClientMock implements WorldCheckNotifierServiceClient {

  @Override
  public void sendUri(ResourceIdentifier identifier) {
    log.info("WorldCheck received URI -> " + identifier.getUri());
  }
}
