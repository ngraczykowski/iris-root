package com.silenteight.payments.bridge.firco.core.alertmessage.model;

import java.util.EnumSet;

/**
 * The status of the alert message.
 */
public enum AlertMessageStatus {
  /**
   * The message has been received in the API controller and its status recorded in the database.
   * The alert awaits in queue for being stored in the database.
   */
  RECEIVED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.of(STORED, ACCEPTED, REJECTED_OVERFLOWED,
          REJECTED_OUTDATED, /* temporary */ RECOMMENDED);
    }
  },

  /**
   * The message has been stored in the database and is on its way for being accepted.
   */
  STORED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.of(ACCEPTED, REJECTED_OUTDATED, /* temporary */ RECOMMENDED);
    }
  },

  /**
   * The message has been accepted for processing in the bridge:
   * <ul>
   *   <li>the data has been extracted to the data source,</li>
   *   <li>the alert has been registered with Adjudication Engine,</li>
   *   <li>and the alert has been added to an analysis for generating recommendation.</li>
   * </ul>
   */
  ACCEPTED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.of(RECOMMENDED, REJECTED_OUTDATED);
    }
  },
  /**
   * The message recommendation has been generated and is awaiting sending back to the requesting
   * party.
   * This is a final state.
   */
  RECOMMENDED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.noneOf(AlertMessageStatus.class);
    }
  },
  //  /**
  //   * The message was not delivered correctly to the requesting party.
  //   * This may be related to network problems or other problems on the client's side.
  //   * <p/>
  //   * This is a final state.
  //   */
  //  RECOMMENDATION_UNDELIVERED {
  //    @Override
  //    public EnumSet<AlertMessageStatus> allowedTransitions() {
  //      return EnumSet.noneOf(AlertMessageStatus.class);
  //    }
  //  },
  //  /**
  //   * The message was delivered successfully to the requesting party.
  //   * <p/>
  //   * This is a final state.
  //   */
  //  RECOMMENDATION_DELIVERED {
  //    @Override
  //    public EnumSet<AlertMessageStatus> allowedTransitions() {
  //      return EnumSet.noneOf(AlertMessageStatus.class);
  //    }
  //  },
  /**
   * The message has been rejected due to being outdated, i.e., the expected time for processing
   * alert (a.k.a. SLA, or TTL) has expired. The alert should have been recommended as Manual
   * Investigation when in this state.
   * <p/>
   * This is a final state.
   */
  REJECTED_OUTDATED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.noneOf(AlertMessageStatus.class);
    }
  },
  /**
   * The message failed the validation or the extract-transform-load (ETL). The alert should have
   * been recommended as Manual Investigation when in this state.
   * <p/>
   * This is a final state.
   */
  REJECTED_DAMAGED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.noneOf(AlertMessageStatus.class);
    }
  },
  /**
   * The number of queued alerts exceeded configured threshold and the message has been rejected
   * instantly. The alert should have been recommended as Manual Investigation when in this state.
   * <p/>
   * This is a final state.
   */
  REJECTED_OVERFLOWED {
    @Override
    public EnumSet<AlertMessageStatus> allowedTransitions() {
      return EnumSet.noneOf(AlertMessageStatus.class);
    }
  };

  public abstract EnumSet<AlertMessageStatus> allowedTransitions();

  public boolean isTransitionAllowed(AlertMessageStatus status) {
    return this.allowedTransitions().contains(status);
  }

  public boolean isFinal() {
    return this.allowedTransitions().isEmpty();
  }

}
