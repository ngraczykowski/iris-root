package com.silenteight.payments.bridge.firco.core.alertmessage.port;

public interface RejectOutdatedAlertMessagesUseCase {

  /**
   * @return flag if all outdated alert messages have been processed.
   */
  boolean process(int chunkSize);

}
