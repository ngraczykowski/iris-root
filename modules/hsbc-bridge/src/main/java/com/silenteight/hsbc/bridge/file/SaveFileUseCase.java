package com.silenteight.hsbc.bridge.file;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
class SaveFileUseCase {

  private final ResourceSaver resourceSaver;

  public ResourceIdentifier save(InputStream file, String fileName) {
    try {
      return resourceSaver.save(file, fileName);
    } catch (IOException e) {
      throw new FileTransferException(e.getCause());
    }
  }
}
