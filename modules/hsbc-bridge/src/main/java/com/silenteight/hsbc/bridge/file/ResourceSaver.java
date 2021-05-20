package com.silenteight.hsbc.bridge.file;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceSaver {

  ResourceIdentifier save(InputStream file, String name) throws IOException;
}
