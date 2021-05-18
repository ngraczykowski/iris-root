package com.silenteight.hsbc.bridge.report;

import java.util.Collection;

public interface AlertSender {

  void sendAlerts(Collection<Alert> alerts);

  interface Alert {

    //FIXME clarify what is needed here
    String getMetadata();
  }
}
