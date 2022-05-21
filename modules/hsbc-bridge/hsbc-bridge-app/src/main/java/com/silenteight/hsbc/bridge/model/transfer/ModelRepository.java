package com.silenteight.hsbc.bridge.model.transfer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public interface ModelRepository {

  URI saveModel(String modelUrl, String name) throws IOException;
  URI saveModel(InputStream inputStream, String name) throws IOException;
}
