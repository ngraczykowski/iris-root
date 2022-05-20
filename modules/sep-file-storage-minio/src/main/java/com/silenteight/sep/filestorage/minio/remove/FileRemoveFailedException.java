package com.silenteight.sep.filestorage.minio.remove;

public class FileRemoveFailedException extends RuntimeException {

  private static final long serialVersionUID = 5617214660812792581L;

  public FileRemoveFailedException(String message) {
    super(message);
  }
}
