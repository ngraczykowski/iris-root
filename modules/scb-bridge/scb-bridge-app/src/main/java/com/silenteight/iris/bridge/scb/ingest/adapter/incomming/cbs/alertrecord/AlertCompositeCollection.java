/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
class AlertCompositeCollection {

  private final List<ValidAlertComposite> validAlerts;
  private final List<InvalidAlert> invalidAlerts;
}
