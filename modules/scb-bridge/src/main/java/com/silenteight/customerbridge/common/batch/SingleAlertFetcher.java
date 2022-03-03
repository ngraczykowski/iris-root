package com.silenteight.customerbridge.common.batch;

import com.silenteight.proto.serp.v1.alert.Alert;

import java.util.Optional;

public interface SingleAlertFetcher {

  Optional<Alert> fetch(String systemId);
}
