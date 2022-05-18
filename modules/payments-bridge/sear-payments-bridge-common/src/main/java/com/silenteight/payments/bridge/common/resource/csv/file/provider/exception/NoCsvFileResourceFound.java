
package com.silenteight.payments.bridge.common.resource.csv.file.provider.exception;

public class NoCsvFileResourceFound extends RuntimeException {

  private static final long serialVersionUID = -3091393385432860418L;

  public NoCsvFileResourceFound(String message) {
    super(message);
  }
}
