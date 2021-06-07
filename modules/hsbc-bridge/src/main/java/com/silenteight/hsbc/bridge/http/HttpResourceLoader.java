package com.silenteight.hsbc.bridge.http;

import com.silenteight.hsbc.bridge.file.DownloadResourceUseCase;
import com.silenteight.hsbc.bridge.file.ResourceDoesNotExistException;
import com.silenteight.hsbc.bridge.file.ResourceIdentifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;

class HttpResourceLoader implements DownloadResourceUseCase {

  @Override
  public InputStream download(
      ResourceIdentifier resourceIdentifier) throws UncheckedIOException,
      ResourceDoesNotExistException {
    try {
      return URI.create(resourceIdentifier.getUri()).toURL().openStream();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
