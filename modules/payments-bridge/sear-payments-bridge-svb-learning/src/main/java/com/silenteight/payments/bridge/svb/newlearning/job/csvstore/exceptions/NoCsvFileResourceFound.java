
package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.exceptions;

public class NoCsvFileResourceFound extends RuntimeException {

  private static final long serialVersionUID = -3091393385432860418L;

  public NoCsvFileResourceFound(String message) {
    super(message);
  }
}
