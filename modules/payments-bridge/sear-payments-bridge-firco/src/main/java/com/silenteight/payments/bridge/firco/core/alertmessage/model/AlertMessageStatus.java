package com.silenteight.payments.bridge.firco.core.alertmessage.model;

/**
 * The status of the alert message.
 */
public enum AlertMessageStatus {
  /**
   * The message has been received in the API controller and its status recorded in the database.
   * The alert awaits in queue for being stored in the database.
   */
  RECEIVED,
  /**
   * The message has been stored in the database and is on its way for being accepted.
   */
  STORED,
  /**
   * The message has been accepted for processing in the bridge:
   * <ul>
   *   <li>the data has been extracted to the data source,</li>
   *   <li>the alert has been registered with Adjudication Engine,</li>
   *   <li>and the alert has been added to an analysis for generating recommendation.</li>
   * </ul>
   */
  ACCEPTED,
  /**
   * The message recommendation has been generated and potentially sent back to the requesting
   * party.
   * <p/>
   * This is a final state.
   */
  RECOMMENDED,
  /**
   * The message has been rejected due to being outdated, i.e., the expected time for processing
   * alert (a.k.a. SLA, or TTL) has expired. The alert should have been recommended as Manual
   * Investigation when in this state.
   * <p/>
   * This is a final state.
   */
  REJECTED_OUTDATED,
  /**
   * The message failed the validation or the extract-transform-load (ETL). The alert should have
   * been recommended as Manual Investigation when in this state.
   * <p/>
   * This is a final state.
   */
  REJECTED_DAMAGED,
  /**
   * The number of queued alerts exceeded configured threshold and the message has been rejected
   * instantly. The alert should have been recommended as Manual Investigation when in this state.
   * <p/>
   * This is a final state.
   */
  REJECTED_OVERFLOWED,
}
