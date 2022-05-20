package com.silenteight.sep.filestorage.minio.manager;

public class BucketManageException extends RuntimeException {

  private static final long serialVersionUID = 5617274360812792621L;

  public BucketManageException(String message) {
    super(message);
  }
}
