package com.silenteight.payments.bridge.app.integration.newlearning;

public class S3CsvFileResourceProviderException extends RuntimeException {

  S3CsvFileResourceProviderException(String message) {
    super(message);
  }

  S3CsvFileResourceProviderException(Throwable cause) {
    super(cause);
  }
}
