package com.silenteight.payments.bridge.common.model;

import lombok.Value;

import java.util.UUID;

@Value
public class SimpleAlertId implements AlertId {

  UUID alertId;

}
