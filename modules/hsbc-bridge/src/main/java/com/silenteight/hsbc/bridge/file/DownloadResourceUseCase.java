package com.silenteight.hsbc.bridge.file;

import java.io.InputStream;
import java.io.UncheckedIOException;

public interface DownloadResourceUseCase {

  InputStream download(ResourceIdentifier resourceIdentifier) throws
      UncheckedIOException, ResourceDoesNotExistException;
}
