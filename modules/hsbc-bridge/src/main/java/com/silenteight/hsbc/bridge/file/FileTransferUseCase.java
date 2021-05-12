package com.silenteight.hsbc.bridge.file;

import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
class FileTransferUseCase {

  private final String filesDirectory;

  public void transfer(InputStream file, String fileName) {

    try {
      Files.copy(file, getSaveDirectoryPath(fileName));
    } catch (FileAlreadyExistsException e) {
      throw new FileExistsException("File already exists");
    } catch (IOException e) {
      throw new FileTransferException(e.getMessage(), e.getCause());
    }
  }

  private Path getSaveDirectoryPath(String fileName) {
    return Path.of(filesDirectory + "/" + fileName);
  }
}
