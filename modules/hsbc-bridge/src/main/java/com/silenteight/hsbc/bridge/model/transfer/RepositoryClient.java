package com.silenteight.hsbc.bridge.model.transfer;

import java.io.IOException;

public interface RepositoryClient {

  byte[] updateModel(String uri) throws IOException;
}
