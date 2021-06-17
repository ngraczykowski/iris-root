package com.silenteight.hsbc.bridge.model.transfer;

import java.io.IOException;
import java.net.URI;

public interface ModelRepository {

  URI saveModel(String modelUrl, String name) throws IOException;
}
