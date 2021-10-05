package com.silenteight.payments.bridge.firco.alertmessage.model;

public enum DeliveryStatus {

  /**
   * Not-applicable. The alert is not in the final state.
   */
  NA,

  /**
   * The recommendation/rejection was delivered to the requesting party.
   */
  DELIVERED,

  /**
   * The recommendation/rejection could not be delivered to the requesting party at the requested
   * time.
   */
  UNDELIVERED;
}
