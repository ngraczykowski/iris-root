package com.silenteight.payments.bridge.firco.alertmessage.model;

public enum DeliveryStatus {

  /**
   * Not-applicable. The alert is not in the final state.
   */
  NA(false),

  /**
   * Delivery in progress. intermediate state.
   */
  PENDING(false),

  /**
   * The recommendation/rejection was delivered to the requesting party.
   * The final state.
   */
  DELIVERED(true),

  /**
   * The recommendation/rejection could not be delivered to the requesting party at the requested
   * time.
   * The final state.
   */
  UNDELIVERED(true);

  private final boolean isFinal;

  DeliveryStatus(boolean isFinal) {
    this.isFinal = isFinal;
  }

  public boolean isFinal() {
    return isFinal;
  }
}
