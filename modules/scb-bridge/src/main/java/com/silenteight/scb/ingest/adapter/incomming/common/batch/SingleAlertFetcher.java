package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.Optional;

public interface SingleAlertFetcher {

  Optional<Alert> fetch(String systemId);
}
