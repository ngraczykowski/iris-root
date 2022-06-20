/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.List;

public record IngestedAlertsStatus(
    List<Alert> success,
    List<Alert> failed) {

}
