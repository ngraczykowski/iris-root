package com.silenteight.payments.bridge.app.s3;

public class S3CsvFileResourceProviderException extends RuntimeException {

  private static final long serialVersionUID = 1563320108077564372L;

  S3CsvFileResourceProviderException(String message) {
    super(message);
  }

  S3CsvFileResourceProviderException(Throwable cause) {
    super(cause);
  }
}
