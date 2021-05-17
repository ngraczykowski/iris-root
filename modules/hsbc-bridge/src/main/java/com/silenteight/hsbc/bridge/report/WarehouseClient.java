package com.silenteight.hsbc.bridge.report;

import java.util.Collection;

public interface WarehouseClient {

  void sendAlerts(Collection<Alert> alerts);

  interface Alert {

    //FIXME clarify what is needed here
    String getMetadata();
  }
}
