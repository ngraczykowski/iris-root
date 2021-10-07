package com.silenteight.payments.bridge.firco.alertmessage.port;

public interface OutdatedAlertMessagesUseCase {

  /**
   * Process the outdated messages.
   *
   * @return flag if all outdated alert messages have been processed.
   */
  boolean process(int chunkSize);

}
