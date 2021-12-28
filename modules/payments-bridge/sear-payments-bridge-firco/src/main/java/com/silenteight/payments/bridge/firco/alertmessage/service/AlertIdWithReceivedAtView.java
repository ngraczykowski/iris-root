package com.silenteight.payments.bridge.firco.alertmessage.service;

import java.sql.Timestamp;
import java.util.UUID;

public interface AlertIdWithReceivedAtView {

  UUID getId();

  Timestamp getReceivedAt();

}
