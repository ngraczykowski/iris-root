package com.silenteight.hsbc.bridge.file;

import java.io.InputStream;
import java.io.UncheckedIOException;

public interface SaveResourceUseCase {

  ResourceIdentifier save(InputStream inputStream, String id) throws UncheckedIOException;
}
