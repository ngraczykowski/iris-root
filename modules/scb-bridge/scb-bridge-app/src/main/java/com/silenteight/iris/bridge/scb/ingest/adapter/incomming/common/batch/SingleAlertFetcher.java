/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.Optional;

public interface SingleAlertFetcher {

  Optional<Alert> fetch(String systemId);
}
