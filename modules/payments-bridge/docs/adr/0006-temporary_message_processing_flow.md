# ADR 0006: Temporary message processing flow

## Goal

To determine how to process alert messages and end up with Manual Investigation recommendations.
The flow is developed for testing conceptual software architecture regarding robust processing of real-time alerts.

## Decision

The high-level pseudo-code flow is this:

- receive message at the REST controller and queue for further processing,
- consume the queued messages and recommend them as manual investigation,
- schedule periodically checks for outdated messages, rejecting them with manual investigation.

```java
    /////////////////////////////// RECEIVING
    var messagesPending = saveMessage();  // msg to DB and status as RECEIVED
    respondToHttpClientWithAck(); // WTF HOW TO DO THIS?
    if (isHighWaterMarkBreached(messagesPending)) {
      rejectMessage(REJECTED_OVERFLOWED); // status as REJECTED_OVERFLOWED
    }
    try {
      queueMessageStoredEvent(); // retry x3, message name only (i.e., UUID)
      markAsStored();  // status as STORED, but only if RECEIVED, ignore other states
    } catch () {
      // return 502 Service unavailable
    }

    /////////////////////////////// OUTDATING SCHEDULER
    // for each msg in status (RECEIVED, STORED) and deadline exceeded: -- doable with single SELECT FOR UPDATE run periodically
    rejectMessage(REJECTED_OUTDATED); // status as REJECTED_OUTDATED, recommendation: MANUAL_INVESTIGATION
    // Problem: each instance has the scheduler running. How to lock between instances, so just a single schedule runs.
    // Possible solutions:
    //   - net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
    //   - separate service that schedules rabbit mq messages that trigger the check
    //   - QuartzScheduler?

    /////////////////////////////// DELIVERY (ACCEPTING?) (consuming the MessageStored events)
    // in single transaction:
    messageStatus = transitionMessageToStatus(messageName, ACCEPTED); // Transision only from RECEIVED and STORED states.
    if (messageStatus.isFinal()) {
      return;
    }
    if (messageStatus.isOutdated()) {
      rejectMessage(REJECTED_OUTDATED);
    } else if (/*hack for the time being*/ true) {
      recommendMessage(MANUAL_INVESTIGATION); // message status as RECOMMENDED
    }
    // further down the road:
    payload = fetchPayloadFromDb();
    // so on and so forth...
    // and they lived happily ever after...
```
