package com.silenteight.hsbc.bridge.file;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class WorldCheckAdapter implements WorldCheckNotifierServiceClient {

  @Override
  public void sendUri(ResourceIdentifier identifier) {
    // TODO Send file URI to WorldCheck API
    log.warn("WorldCheck API was not implemented yet - use dev profile instead");
  }
}
